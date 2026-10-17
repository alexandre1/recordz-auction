package com.example.recordz.service;

import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ArticleDynamicDataService {

    private final DataSource dataSource;

    public ArticleDynamicDataService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // --------a-----------------------------------------------------------------
    // LANGUE
    // -------------------------------------------------------------------------

    /** Ramène n'importe quelle valeur ("fr", "de_CH", "EN"...) à FR / DE / EN. */
    private static String normalize(String lang) {
        if (lang == null) return "FR";
        String l = lang.trim().toLowerCase(Locale.ROOT);
        if (l.startsWith("de")) return "DE";
        if (l.startsWith("en")) return "EN";
        return "FR";
    }

    // -------------------------------------------------------------------------
    // CEPAGES (pas de colonne de langue)
    // -------------------------------------------------------------------------

    public List<LabelValue> loadCepages() {
        return querySimple("SELECT id_cepage, nom FROM cepage ORDER BY nom");
    }

    // -------------------------------------------------------------------------
    // CATÉGORIES & SOUS-CATÉGORIES
    // -------------------------------------------------------------------------

    public List<LabelValue> loadCategories(String lang) {
        return queryLocalized("categorie_libelle_langue", "ref_categorie", lang);
    }

    /**
     * Sous-catégories d'une catégorie (ref_subcategorie n'est unique que dans le
     * couple (ref_categorie, ref_subcategorie)). Libellé dans la langue demandée,
     * avec repli sur le français si la traduction n'existe pas.
     */
    public List<LabelValue> loadSubCategories(int categoryId, String lang) {
        String sql = """
            SELECT s.ref_subcategorie, COALESCE(lt.libelle, lf.libelle) AS libelle
            FROM subcategorie_libelle_langue s
            JOIN langue  gf ON gf.`key` = 'FR' AND s.ref_langue = gf.id_langue
            JOIN libelle lf ON s.ref_libelle = lf.id_libelle
            LEFT JOIN langue gt ON gt.`key` = ?
            LEFT JOIN subcategorie_libelle_langue st
                   ON st.ref_categorie    = s.ref_categorie
                  AND st.ref_subcategorie = s.ref_subcategorie
                  AND st.ref_langue       = gt.id_langue
            LEFT JOIN libelle lt ON st.ref_libelle = lt.id_libelle
            WHERE s.ref_categorie = ?
            ORDER BY libelle
            """;
        List<LabelValue> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalize(lang));
            ps.setInt(2, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new LabelValue(rs.getString(1), rs.getString(2)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement sous-catégories", e);
        }
        return result;
    }

    /** Libellé d'une sous-catégorie à partir du COUPLE (catégorie, sous-catégorie). */
    public Optional<String> loadSubCategoryLabel(int categoryId, int subCategoryId, String lang) {
        String sql = """
            SELECT COALESCE(lt.libelle, lf.libelle)
            FROM subcategorie_libelle_langue s
            JOIN langue  gf ON gf.`key` = 'FR' AND s.ref_langue = gf.id_langue
            JOIN libelle lf ON s.ref_libelle = lf.id_libelle
            LEFT JOIN langue gt ON gt.`key` = ?
            LEFT JOIN subcategorie_libelle_langue st
                   ON st.ref_categorie    = s.ref_categorie
                  AND st.ref_subcategorie = s.ref_subcategorie
                  AND st.ref_langue       = gt.id_langue
            LEFT JOIN libelle lt ON st.ref_libelle = lt.id_libelle
            WHERE s.ref_categorie = ? AND s.ref_subcategorie = ?
            LIMIT 1
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalize(lang));
            ps.setInt(2, categoryId);
            ps.setInt(3, subCategoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.ofNullable(rs.getString(1)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement libellé sous-catégorie", e);
        }
    }

    // -------------------------------------------------------------------------
    // VINS
    // -------------------------------------------------------------------------

    public List<LabelValue> loadWineCountries() {
        return querySimple(
                "SELECT id_pays_region_vin, nom FROM pays_region_vin ORDER BY nom"
        );
    }

    public List<LabelValue> loadWineRegions(String country) {
        String sql = """
            SELECT pays_region_vin.id_pays_region_vin, pays_region_vin.nom
            FROM pays_region_vin
            JOIN pays_present ON ref_pays = id_pays_present
            WHERE pays_present.nom = ?
              AND parent_id IS NOT NULL
            ORDER BY pays_region_vin.nom
            """;
        return queryWithParam(sql, country);
    }

    public List<LabelValue> loadWineTypes(String lang) {
        return queryLocalized("type_de_vin_libelle_langue", "ref_type_de_vin", lang);
    }

    // -------------------------------------------------------------------------
    // IMMOBILIER
    // -------------------------------------------------------------------------

    public List<LabelValue> loadCountries() {
        return querySimple(
                "SELECT id_pays_present, nom FROM pays_present ORDER BY nom"
        );
    }

    /** Table canton_fr / canton_de / canton_en ; repli sur canton_fr si la table n'existe pas. */
    public List<LabelValue> loadCantons(String lang) {
        String l = normalize(lang).toLowerCase(Locale.ROOT);
        try {
            return querySimple("SELECT id_canton, nom FROM canton_" + l + " ORDER BY nom");
        } catch (RuntimeException e) {
            if ("fr".equals(l)) throw e;
            return querySimple("SELECT id_canton, nom FROM canton_fr ORDER BY nom");
        }
    }

    public List<LabelValue> loadDepartements() {
        return querySimple("SELECT code, nom FROM departement ORDER BY nom");
    }

    public List<LabelValue> loadLocationOrBuyOptions(String lang) {
        return queryLocalized("location_ou_achat_libelle_langue", "ref_location_ou_achat", lang);
    }

    // -------------------------------------------------------------------------
    // VÉHICULES
    // -------------------------------------------------------------------------

    public List<LabelValue> loadFuelTypes(String lang) {
        return queryLocalized("type_essence_libelle_langue", "ref_type_essence", lang);
    }

    public List<LabelValue> loadGearboxTypes(String lang) {
        return queryLocalized("boite_de_vitesse_libelle_langue", "ref_boite_de_vitesse", lang);
    }

    // -------------------------------------------------------------------------
    // TV / ÉCRAN
    // -------------------------------------------------------------------------

    public List<LabelValue> loadScreenTypes(String lang) {
        return queryLocalized("type_ecran_libelle_langue", "ref_type_ecran", lang);
    }

    // -------------------------------------------------------------------------
    // JEUX
    // -------------------------------------------------------------------------

    public List<LabelValue> loadGameTypes(String lang) {
        return queryLocalized("type_de_jeux_libelle_langue", "ref_type_de_jeux", lang);
    }

    // -------------------------------------------------------------------------
    // ENCHÈRES
    // -------------------------------------------------------------------------

    public List<LabelValue> loadAuctionOptions(String lang) {
        return switch (normalize(lang)) {
            case "EN" -> List.of(new LabelValue("yes", "Yes"),  new LabelValue("no", "No"));
            case "DE" -> List.of(new LabelValue("yes", "Ja"),   new LabelValue("no", "Nein"));
            default   -> List.of(new LabelValue("yes", "Oui"),  new LabelValue("no", "Non"));
        };
    }

    // -------------------------------------------------------------------------
    // ÉTAT
    // -------------------------------------------------------------------------

    public List<LabelValue> loadConditionOptions(String lang) {
        return queryLocalized("etat_libelle_langue", "ref_etat", lang);
    }

    // -------------------------------------------------------------------------
    // PAIEMENT & LIVRAISON
    // -------------------------------------------------------------------------

    public List<LabelValue> loadPaymentConditions(String lang) {
        return queryLocalized("condition_payement_libelle_langue", "ref_condition_payement", lang);
    }

    public List<LabelValue> loadDeliveryConditions(String lang) {
        return queryLocalized("condition_livraison_libelle_langue", "ref_mode_de_livraison", lang);
    }

    // -------------------------------------------------------------------------
    // UTILITAIRES JDBC
    // -------------------------------------------------------------------------

    /**
     * Charge une table "xxx_libelle_langue" dans la langue demandée.
     * La liste est construite à partir des lignes FRANÇAISES (toujours présentes) ;
     * le libellé est celui de la langue demandée s'il existe, sinon le français.
     * (table et refCol sont des constantes du code, jamais des saisies utilisateur.)
     */
    private List<LabelValue> queryLocalized(String table, String refCol, String lang) {
        String sql = """
            SELECT b.%1$s, COALESCE(lt.libelle, lf.libelle) AS libelle
            FROM %2$s b
            JOIN langue  gf ON gf.`key` = 'FR' AND b.ref_langue = gf.id_langue
            JOIN libelle lf ON b.ref_libelle = lf.id_libelle
            LEFT JOIN langue gt ON gt.`key` = ?
            LEFT JOIN %2$s bt ON bt.%1$s = b.%1$s AND bt.ref_langue = gt.id_langue
            LEFT JOIN libelle lt ON bt.ref_libelle = lt.id_libelle
            ORDER BY libelle
            """.formatted(refCol, table);
        List<LabelValue> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalize(lang));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new LabelValue(rs.getString(1), rs.getString(2)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement " + table, e);
        }
        return result;
    }

    private List<LabelValue> queryWithParam(String sql, String param) {
        List<LabelValue> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new LabelValue(rs.getString(1), rs.getString(2)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement données", e);
        }
        return result;
    }

    private List<LabelValue> querySimple(String sql) {
        List<LabelValue> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new LabelValue(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement données", e);
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // RECORD
    // -------------------------------------------------------------------------

    public record LabelValue(String value, String label) {
        @Override public String toString() { return label; }
    }
}
