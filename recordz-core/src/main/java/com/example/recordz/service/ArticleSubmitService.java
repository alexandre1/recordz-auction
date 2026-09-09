package com.example.recordz.service;

import com.example.recordz.model.domain.dto.ArticleFormData;
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

/**
 * Persistance d'un article depuis le formulaire d'ajout.
 * Équivalent du bloc d'INSERT dans le Perl loadAddArticle().
 */
@Service
public class ArticleSubmitService {

    private final DataSource dataSource;
    private final Path uploadDir;

    public ArticleSubmitService(DataSource dataSource,
                                @Value("${app.upload.dir}") String uploadDirPath) {
        this.dataSource = dataSource;
        this.uploadDir  = Paths.get(uploadDirPath);
    }

    /**
     * Insère un article en base et retourne son id_article.
     */
    public int save(ArticleFormData data) {

        long owned = resolveCurrentUserId();

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
                ?,?,1,9,0,
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
            return newId;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion article", e);
        }
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

    // ── 1. Remplace resolveCurrentUserId() ───────────────────────
    private long resolveCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                if (principal instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser oidc) {
                    String email = oidc.getAttribute("email");
                    if (email != null) {
                        // Récupérer l'id depuis la DB via email
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
