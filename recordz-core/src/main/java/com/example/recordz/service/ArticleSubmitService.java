package com.example.recordz.service;

import com.example.recordz.model.domain.dto.ArticleFormData;
import com.example.recordz.model.domain.dto.ArticleSaveResult;
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
 * Intégration Entrupy :
 *  - seules les catégories listées dans CATEGORIES_REQUIRANT_ENTRUPY déclenchent
 *    le circuit d'authentification (customer_item_id + statut "pending").
 *  - les autres catégories sont publiées immédiatement (pas de modération).
 *
 * Intégration vidéo :
 *  - si data.videoPath pointe vers un fichier temporaire (déposé par
 *    VideoUploadController dans app.video-temp.dir via le flux QR code /
 *    Redis Pub/Sub côté ArticleFormView), il est déplacé vers app.video.dir
 *    une fois l'id_article connu, et la colonne video_path mise à jour.
 *  - data.videoPath == null/blank : article sans vidéo, comportement inchangé.
 */
@Service
public class ArticleSubmitService {

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
     * Insère un article en base et retourne son id_article.
     */
    public ArticleSaveResult save(ArticleFormData data) {

        long owned = resolveCurrentUserId();
        boolean requiresEntrupy = requiresEntrupyAuthentication(data);
        int statutInitial = requiresEntrupy ? STATUT_EN_ATTENTE_AUTH_ENTRUPY : STATUT_PUBLIE;

        String sql = """
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

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

            int newId = -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) newId = keys.getInt(1);
            }

            if (newId > 0) {
                String ext      = resolveExtension(data.pochette);
                String pochette = "article_" + newId + ext;

                try (PreparedStatement upd = conn.prepareStatement(
                        "UPDATE article SET pochette = ? WHERE id_article = ?")) {
                    upd.setString(1, pochette);
                    upd.setInt(2, newId);
                    upd.executeUpdate();
                }

                if (requiresEntrupy) {
                    String customerItemId = CUSTOMER_ITEM_ID_PREFIX + newId;
                    try (PreparedStatement updEntrupy = conn.prepareStatement(
                            "UPDATE article SET entrupy_customer_item_id = ?, entrupy_status = 'pending' " +
                                    "WHERE id_article = ?")) {
                        updEntrupy.setString(1, customerItemId);
                        updEntrupy.setInt(2, newId);
                        updEntrupy.executeUpdate();
                    }
                }

                // Vidéo : déplace le fichier temporaire (déposé par
                // VideoUploadController) vers le stockage définitif, puis met
                // à jour la colonne video_path. Rien ne se passe si aucune
                // vidéo n'a été reçue (data.videoPath null/blank).
                if (data.videoPath != null && !data.videoPath.isBlank()) {
                    String finalVideoPath = moveVideoToFinalStorage(data.videoPath, newId);
                    if (finalVideoPath != null) {
                        try (PreparedStatement updVideo = conn.prepareStatement(
                                "UPDATE article SET video_path = ? WHERE id_article = ?")) {
                            updVideo.setString(1, finalVideoPath);
                            updVideo.setInt(2, newId);
                            updVideo.executeUpdate();
                        }
                    }
                }

                // ✅ Lier l'article au vendeur
                try (PreparedStatement mv = conn.prepareStatement(
                        "INSERT INTO met_en_vente (ref_vendeur, ref_article, date_stock) VALUES (?, ?, CURDATE())")) {
                    mv.setLong(1, owned);
                    mv.setInt(2, newId);
                    mv.executeUpdate();
                }

                if (data.pochetteStream != null) {
                    saveImage(data.pochetteStream, pochette);
                }
            }
            return new ArticleSaveResult(newId, requiresEntrupy);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion article", e);
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
     * VideoUploadController) vers le stockage définitif (app.video.dir),
     * renommé selon l'id_article généré. Retourne le chemin final, ou null
     * si le déplacement échoue — dans ce cas l'article est quand même créé,
     * simplement sans vidéo, plutôt que de faire échouer toute la publication
     * pour un problème de fichier.
     */
    private String moveVideoToFinalStorage(String tempVideoPath, int articleId) {
        try {
            Path source = Paths.get(tempVideoPath);
            if (!Files.exists(source)) {
                return null; // fichier temporaire déjà nettoyé ou chemin invalide
            }

            Files.createDirectories(videoDir);

            String ext = resolveExtension(source.getFileName().toString());
            String filename = "article_" + articleId + ext;
            Path dest = videoDir.resolve(filename);

            try {
                Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);
            } catch (java.nio.file.FileSystemException fsEx) {
                // Cas où temp et stockage définitif sont sur des systèmes de
                // fichiers différents (Files.move atomique impossible) :
                // repli sur copie + suppression.
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(source);
            }

            return dest.toString();

        } catch (Exception e) {
            // Ne fait pas échouer la publication de l'article pour un souci
            // de déplacement de fichier vidéo — l'article reste utilisable
            // sans vidéo, à ré-uploader manuellement si besoin.
            return null;
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

    private long resolveCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
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
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return 0L;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value != null) ps.setInt(index, value);
        else ps.setNull(index, Types.INTEGER);
    }
}
