package com.example.recordz.service;

import com.example.recordz.model.domain.dto.ArticleFormData;
import com.example.recordz.model.domain.dto.ArticleSaveResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Persistance d'un article depuis le formulaire d'ajout.
 * Équivalent du bloc d'INSERT dans le Perl loadAddArticle().
 *
 * Transactionnalité :
 *  - toutes les écritures SQL (article, mises à jour pochette / Entrupy / vidéo,
 *    met_en_vente) s'exécutent dans UNE seule transaction JDBC : tout ou rien.
 *  - les écritures sur le système de fichiers (image, vidéo) ne peuvent pas
 *    participer à une transaction SQL : elles sont donc faites APRÈS le commit,
 *    avec compensation en cas d'échec (voir save()).
 *
 * Intégration Entrupy :
 *  - seules les catégories listées dans CATEGORIES_REQUIRANT_ENTRUPY déclenchent
 *    le circuit d'authentification (customer_item_id + statut "pending").
 *  - les autres catégories sont publiées immédiatement (pas de modération).
 *
 * Intégration vidéo :
 *  - si data.videoPath pointe vers un fichier temporaire (déposé par
 *    VideoUploadController dans app.video-temp.dir via le flux QR code /
 *    Redis Pub/Sub côté ArticleFormView), il est déplacé vers app.video.dir
 *    une fois la transaction validée, et video_path est renseigné dans la
 *    transaction (chemin final calculé à l'avance).
 *  - si le déplacement échoue après le commit, video_path est remis à NULL :
 *    l'article reste publié, simplement sans vidéo.
 *  - data.videoPath == null/blank : article sans vidéo, comportement inchangé.
 */
@Service
public class ArticleSubmitService {

    private static final Logger log = LoggerFactory.getLogger(ArticleSubmitService.class);

    // TODO: remplacer par les véritables id de ref_categorie nécessitant Entrupy
    // (typiquement maroquinerie/montres/bijoux — à confirmer selon votre référentiel)
    private static final Set<Integer> CATEGORIES_REQUIRANT_ENTRUPY = Set.of(
            1, // FASHION_FEMME.
            2,  //FASHION_HOMME
            34, //MONTRE
            35 //BIJOUX
    );

    // TODO: remplacer par les véritables id dans ref_statut
    private static final int STATUT_PUBLIE = 1;
    private static final int STATUT_EN_ATTENTE_AUTH_ENTRUPY = 2;

    private static final String CUSTOMER_ITEM_ID_PREFIX = "CHIC-";

    private static final String INSERT_ARTICLE_SQL = """
            INSERT INTO article (
                nom, label, prix, marque,
                ref_categorie, ref_subcategorie,
                ref_condition_payement, ref_mode_de_livraison, ref_etat,
                quantite, enchere, enchere_date_debut, enchere_date_fin,
                pochette, owned, ref_statut, ref_depot, ref_genre,
                etat, lang, date,
                nb_cheveaux, nb_cylindre, nb_km,
                premiere_immatriculation, annee,
                essence_ou_diesel, ref_boite_de_vitesse, clima,
                auteur,
                ref_location_ou_achat, ref_pays, ref_canton, ref_departement,
                lieu, adresse, npa, nb_piece, surface_habitable,
                superficie_terrain, annee_construction,
                ref_pays_region_vin, ref_cepage, ref_type_de_vin,
                ref_type_de_jeux,
                acteurs, realisateur, duree,
                ref_type_ecran, dimension,
                processeur, ram, disque_dur,
                taille
            ) VALUES (
                ?,?,?,?,
                ?,?,
                ?,?,?,
                ?,?,?,?,
                ?,?,?,9,0,
                1,?,?,
                ?,?,?,
                ?,?,
                ?,?,?,
                ?,
                ?,?,?,?,
                ?,?,?,?,?,
                ?,?,
                ?,?,?,
                ?,
                ?,?,?,
                ?,?,
                ?,?,?,
                ?
            )
            """;

    private final DataSource dataSource;
    private final Path uploadDir;
    private final Path videoDir;

    public ArticleSubmitService(DataSource dataSource,
                                @Value("${app.upload.dir}") String uploadDirPath,
                                @Value("${app.video.dir}") String videoDirPath) {
        this.dataSource = dataSource;
        this.uploadDir  = Paths.get(uploadDirPath);
        this.videoDir   = Paths.get(videoDirPath);
    }

    /**
     * Insère un article en base (transaction unique) puis écrit les fichiers
     * associés, et retourne son id_article.
     *
     * Étapes :
     *  1. résolution de l'utilisateur (avant la transaction : lecture seule) ;
     *  2. transaction SQL : INSERT article + UPDATE groupé + INSERT met_en_vente ;
     *  3. après commit : écriture de l'image (échec => suppression compensatoire
     *     de l'article puis exception), déplacement de la vidéo (échec => video_path
     *     remis à NULL, l'article reste publié).
     */
    public ArticleSaveResult save(ArticleFormData data) {

        long owned = resolveCurrentUserId();
        boolean requiresEntrupy = requiresEntrupyAuthentication(data);
        int statutInitial = requiresEntrupy ? STATUT_EN_ATTENTE_AUTH_ENTRUPY : STATUT_PUBLIE;

        // Vidéo : on ne retient que si le fichier temporaire existe réellement
        Path tempVideo = null;
        if (data.videoPath != null && !data.videoPath.isBlank()) {
            Path candidate = Paths.get(data.videoPath);
            if (Files.exists(candidate)) {
                tempVideo = candidate;
            } else {
                log.warn("Fichier vidéo temporaire introuvable, article créé sans vidéo : {}", data.videoPath);
            }
        }

        int newId = -1;
        String pochette = null;
        Path finalVideo = null;

        // ---------------------------------------------------------------
        // 1) Transaction SQL : tout ou rien
        // ---------------------------------------------------------------
        try (Connection conn = dataSource.getConnection()) {
            boolean previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                newId = insertArticle(conn, data, owned, statutInitial);
                if (newId <= 0) {
                    throw new SQLException("Aucun id_article généré lors de l'insertion");
                }

                pochette = "article_" + newId + resolveExtension(data.pochette);
                if (tempVideo != null) {
                    finalVideo = videoDir.resolve(
                            "article_" + newId + resolveExtension(tempVideo.getFileName().toString()));
                }

                updateArticleAfterInsert(conn, newId, pochette, requiresEntrupy, finalVideo);

                // Lier l'article au vendeur
                try (PreparedStatement mv = conn.prepareStatement(
                        "INSERT INTO met_en_vente (ref_vendeur, ref_article, date_stock) VALUES (?, ?, CURDATE())")) {
                    mv.setLong(1, owned);
                    mv.setInt(2, newId);
                    mv.executeUpdate();
                }

                conn.commit();

            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw e;
            } finally {
                try {
                    conn.setAutoCommit(previousAutoCommit);
                } catch (SQLException ex) {
                    log.warn("Impossible de restaurer l'auto-commit de la connexion", ex);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion article", e);
        }

        // ---------------------------------------------------------------
        // 2) Fichiers : uniquement après le commit
        // ---------------------------------------------------------------
        if (data.pochetteStream != null) {
            try {
                saveImage(data.pochetteStream, pochette);
            } catch (RuntimeException e) {
                // Compensation : sans image, l'article n'a pas de sens -> on l'annule.
                // (la vidéo n'a pas encore été déplacée : le fichier temporaire reste intact)
                compensateDeleteArticle(newId);
                throw e;
            }
        }

        if (finalVideo != null && !moveVideoToFinalStorage(tempVideo, finalVideo)) {
            // L'article reste publié, mais sans vidéo : on évite un video_path orphelin.
            clearVideoPath(newId);
        }

        return new ArticleSaveResult(newId, requiresEntrupy);
    }

    /**
     * Exécute l'INSERT principal sur la connexion transactionnelle fournie
     * et retourne l'id_article généré (ou -1).
     */
    private int insertArticle(Connection conn, ArticleFormData data, long owned, int statutInitial)
            throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(INSERT_ARTICLE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;

            ps.setString(i++, data.nom);
            ps.setString(i++, data.label);
            ps.setDouble(i++, data.prix);
            ps.setString(i++, data.marque != null ? data.marque : "");

            ps.setInt(i++, data.refCategorie);
            setNullableInt(ps, i++, data.refSubcategorie);

            ps.setInt(i++, data.refConditionPayement != null ? data.refConditionPayement : 0);
            ps.setInt(i++, data.refModeDelivraison   != null ? data.refModeDelivraison   : 0);
            setNullableInt(ps, i++, data.refEtat);

            ps.setInt(i++, data.quantite != null ? data.quantite : 1);
            ps.setInt(i++, data.enchere ? 1 : 0);
            ps.setString(i++, data.enchereDateDebut);
            ps.setString(i++, data.enchereDateFin);

            // pochette = '' provisoire, mis à jour juste après avec l'id généré
            ps.setString(i++, "");
            ps.setLong(i++, owned);
            ps.setInt(i++, statutInitial);
            ps.setString(i++, "fr");
            ps.setString(i++, LocalDate.now().toString());

            setNullableInt(ps, i++, data.nbCheveaux);
            setNullableInt(ps, i++, data.nbCylindre);
            setNullableInt(ps, i++, data.nbKm);
            ps.setString(i++, data.premiereImmatriculation);
            ps.setString(i++, data.annee);
            setNullableInt(ps, i++, data.essenceOuDiesel);
            setNullableInt(ps, i++, data.refBoiteDeVitesse);
            if (data.clima != null) ps.setBoolean(i++, data.clima);
            else ps.setNull(i++, Types.TINYINT);

            ps.setString(i++, data.auteur);

            setNullableInt(ps, i++, data.refLocationOuAchat);
            setNullableInt(ps, i++, data.refPays);
            setNullableInt(ps, i++, data.refCanton);
            setNullableInt(ps, i++, data.refDepartement);
            ps.setString(i++, data.lieu);
            ps.setString(i++, data.adresse);
            ps.setString(i++, data.npa);
            setNullableInt(ps, i++, data.nbPiece);
            setNullableInt(ps, i++, data.surfaceHabitable);
            ps.setString(i++, data.superficieTerrain);
            ps.setString(i++, data.anneeConstruction);

            setNullableInt(ps, i++, data.refPaysRegionVin);
            setNullableInt(ps, i++, data.refCepage);
            setNullableInt(ps, i++, data.refTypeDeVin);

            setNullableInt(ps, i++, data.refTypeDeJeux);

            ps.setString(i++, data.acteurs);
            ps.setString(i++, data.realisateur);
            ps.setString(i++, data.duree);

            setNullableInt(ps, i++, data.refTypeEcran);
            setNullableInt(ps, i++, data.dimension);

            if (data.processeur != null) ps.setDouble(i++, data.processeur);
            else ps.setNull(i++, Types.DECIMAL);
            ps.setString(i++, data.ram);
            ps.setString(i++, data.disqueDur);

            ps.setString(i++, data.taille);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        }
    }

    /**
     * UPDATE groupé (remplace les 3 UPDATE séparés d'origine) : pochette,
     * champs Entrupy (si requis) et video_path (si vidéo). Un seul aller-retour
     * dans la transaction.
     */
    private void updateArticleAfterInsert(Connection conn, int articleId, String pochette,
                                          boolean requiresEntrupy, Path finalVideo) throws SQLException {

        StringBuilder sql = new StringBuilder("UPDATE article SET pochette = ?");
        if (requiresEntrupy) {
            sql.append(", entrupy_customer_item_id = ?, entrupy_status = 'pending'");
        }
        if (finalVideo != null) {
            sql.append(", video_path = ?");
        }
        sql.append(" WHERE id_article = ?");

        try (PreparedStatement upd = conn.prepareStatement(sql.toString())) {
            int i = 1;
            upd.setString(i++, pochette);
            if (requiresEntrupy) {
                upd.setString(i++, toCustomerItemId(articleId));
            }
            if (finalVideo != null) {
                upd.setString(i++, finalVideo.toString());
            }
            upd.setInt(i, articleId);
            upd.executeUpdate();
        }
    }

    /**
     * Détermine si la catégorie de l'article nécessite une authentification Entrupy.
     */
    private boolean requiresEntrupyAuthentication(ArticleFormData data) {
        return CATEGORIES_REQUIRANT_ENTRUPY.contains(data.refCategorie);
    }

    /**
     * Utilitaire pour l'UI : reconstruit le customer_item_id sans round-trip DB
     * (utile juste après save() pour afficher le QR code à l'instant T).
     */
    public static String toCustomerItemId(int articleId) {
        return CUSTOMER_ITEM_ID_PREFIX + articleId;
    }

    // -------------------------------------------------------------------------
    // Fichiers (hors transaction SQL)
    // -------------------------------------------------------------------------

    /**
     * Écrit le fichier image dans le répertoire app.upload.dir.
     * Crée le répertoire s'il n'existe pas.
     */
    private void saveImage(InputStream stream, String filename) {
        try {
            Files.createDirectories(uploadDir);
            Path dest = uploadDir.resolve(filename);
            Files.copy(stream, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Erreur écriture image : " + filename, e);
        }
    }

    /**
     * Déplace le fichier vidéo temporaire (app.video-temp.dir, déposé par
     * VideoUploadController) vers le stockage définitif (app.video.dir).
     * Retourne true si le déplacement a réussi, false sinon (l'erreur est loggée).
     */
    private boolean moveVideoToFinalStorage(Path source, Path dest) {
        try {
            Files.createDirectories(videoDir);

            try {
                Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);
            } catch (java.nio.file.FileSystemException fsEx) {
                // Cas où temp et stockage définitif sont sur des systèmes de
                // fichiers différents (Files.move atomique impossible) :
                // repli sur copie + suppression.
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(source);
            }
            return true;

        } catch (Exception e) {
            log.warn("Échec du déplacement de la vidéo {} -> {} : l'article reste publié sans vidéo",
                    source, dest, e);
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Compensations (après commit, en cas d'échec d'une écriture de fichier)
    // -------------------------------------------------------------------------

    /**
     * Remet video_path à NULL si la vidéo n'a pas pu être déplacée.
     */
    private void clearVideoPath(int articleId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE article SET video_path = NULL WHERE id_article = ?")) {
            ps.setInt(1, articleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Impossible de remettre video_path à NULL pour l'article {}", articleId, e);
        }
    }

    /**
     * Supprime l'article (et son lien met_en_vente) dans une transaction dédiée
     * lorsque l'écriture de l'image échoue après le commit.
     */
    private void compensateDeleteArticle(int articleId) {
        try (Connection conn = dataSource.getConnection()) {
            boolean previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM met_en_vente WHERE ref_article = ?")) {
                    ps.setInt(1, articleId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM article WHERE id_article = ?")) {
                    ps.setInt(1, articleId);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw e;
            } finally {
                try {
                    conn.setAutoCommit(previousAutoCommit);
                } catch (SQLException ex) {
                    log.warn("Impossible de restaurer l'auto-commit de la connexion", ex);
                }
            }
        } catch (SQLException | RuntimeException e) {
            log.error("Compensation impossible : article {} à supprimer manuellement", articleId, e);
        }
    }

    /**
     * Extrait l'extension depuis le nom de fichier uploadé.
     * Ex: "photo.jpg" → ".jpg" | null ou inconnu → ".jpg" par défaut
     */
    private String resolveExtension(String filename) {
        if (filename == null || filename.isBlank()) return ".jpg";
        int dot = filename.lastIndexOf('.');
        if (dot >= 0 && dot < filename.length() - 1)
            return "." + filename.substring(dot + 1).toLowerCase();
        return ".jpg";
    }

    // -------------------------------------------------------------------------
    // Résolution de l'utilisateur connecté via Spring Security
    // -------------------------------------------------------------------------

    /**
     * Retourne l'id_personne de l'utilisateur connecté.
     * Lève une exception (avant toute écriture) si l'utilisateur n'est pas
     * résolu, au lieu de renvoyer silencieusement 0 et de créer un article
     * rattaché à un vendeur inexistant.
     */
    private long resolveCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur authentifié");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser oidc) {
            String email = oidc.getAttribute("email");
            if (email != null) {
                try (Connection conn = dataSource.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "SELECT id_personne FROM personne WHERE email = ?")) {
                    ps.setString(1, email);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return rs.getLong(1);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erreur résolution utilisateur connecté", e);
                }
            }
        }
        throw new IllegalStateException("Utilisateur connecté introuvable dans la table personne");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value != null) ps.setInt(index, value);
        else ps.setNull(index, Types.INTEGER);
    }
}
