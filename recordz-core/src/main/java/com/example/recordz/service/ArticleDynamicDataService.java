package com.example.recordz.service;

import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleDynamicDataService {

    private final DataSource dataSource;

    public ArticleDynamicDataService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<LabelValue> loadCepages() {
        return querySimple("SELECT id_cepage, nom FROM cepage ORDER BY nom");
    }

    // -------------------------------------------------------------------------
    // CATÉGORIES & SOUS-CATÉGORIES
    // -------------------------------------------------------------------------

    public List<LabelValue> loadCategories(String lang) {
        String sql = """
            SELECT categorie_libelle_langue.ref_categorie, libelle.libelle
            FROM categorie_libelle_langue
            JOIN libelle ON categorie_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON categorie_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    public List<LabelValue> loadSubCategories(String mainCategoryLabel, String lang) {
        String sql = """
            SELECT subcategorie_libelle_langue.ref_subcategorie, libelle.libelle
            FROM subcategorie_libelle_langue
            JOIN libelle ON subcategorie_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON subcategorie_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
              AND subcategorie_libelle_langue.ref_categorie = (
                  SELECT ref_categorie
                  FROM categorie_libelle_langue
                  JOIN libelle l2 ON categorie_libelle_langue.ref_libelle = l2.id_libelle
                  JOIN langue  la ON categorie_libelle_langue.ref_langue  = la.id_langue
                  WHERE l2.libelle = ? AND la.key = ?
              )
            ORDER BY libelle.libelle
            """;
        return queryThreeParams(sql, lang, mainCategoryLabel, lang);
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
        String sql = """
            SELECT type_de_vin_libelle_langue.ref_type_de_vin, libelle.libelle
            FROM type_de_vin_libelle_langue
            JOIN libelle ON type_de_vin_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON type_de_vin_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // IMMOBILIER
    // -------------------------------------------------------------------------

    public List<LabelValue> loadCountries() {
        return querySimple(
                "SELECT id_pays_present, nom FROM pays_present ORDER BY nom"
        );
    }

    public List<LabelValue> loadCantons(String lang) {
        String table = "canton_" + lang.toLowerCase();
        return querySimple("SELECT id_canton, nom FROM " + table + " ORDER BY nom");
    }

    public List<LabelValue> loadDepartements() {
        return querySimple("SELECT code, nom FROM departement ORDER BY nom");
    }

    public List<LabelValue> loadLocationOrBuyOptions(String lang) {
        String sql = """
            SELECT location_ou_achat_libelle_langue.ref_location_ou_achat, libelle.libelle
            FROM location_ou_achat_libelle_langue
            JOIN libelle ON location_ou_achat_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON location_ou_achat_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // VÉHICULES
    // -------------------------------------------------------------------------

    public List<LabelValue> loadFuelTypes(String lang) {
        String sql = """
            SELECT type_essence_libelle_langue.ref_type_essence, libelle.libelle
            FROM type_essence_libelle_langue
            JOIN libelle ON type_essence_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON type_essence_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    public List<LabelValue> loadGearboxTypes(String lang) {
        String sql = """
            SELECT boite_de_vitesse_libelle_langue.ref_boite_de_vitesse, libelle.libelle
            FROM boite_de_vitesse_libelle_langue
            JOIN libelle ON boite_de_vitesse_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON boite_de_vitesse_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // TV / ÉCRAN
    // -------------------------------------------------------------------------

    public List<LabelValue> loadScreenTypes(String lang) {
        String sql = """
            SELECT type_ecran_libelle_langue.ref_type_ecran, libelle.libelle
            FROM type_ecran_libelle_langue
            JOIN libelle ON type_ecran_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON type_ecran_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // JEUX
    // -------------------------------------------------------------------------

    public List<LabelValue> loadGameTypes(String lang) {
        String sql = """
            SELECT type_de_jeux_libelle_langue.ref_type_de_jeux, libelle.libelle
            FROM type_de_jeux_libelle_langue
            JOIN libelle ON type_de_jeux_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON type_de_jeux_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // ENCHÈRES
    // -------------------------------------------------------------------------

    public List<LabelValue> loadAuctionOptions(String lang) {
        return List.of(
                new LabelValue("yes", "Oui"),
                new LabelValue("no",  "Non")
        );
    }

    // -------------------------------------------------------------------------
    // ÉTAT
    // -------------------------------------------------------------------------

    public List<LabelValue> loadConditionOptions(String lang) {
        String sql = """
            SELECT etat_libelle_langue.ref_etat, libelle.libelle
            FROM etat_libelle_langue
            JOIN libelle ON etat_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON etat_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // PAIEMENT & LIVRAISON
    // -------------------------------------------------------------------------

    public List<LabelValue> loadPaymentConditions(String lang) {
        String sql = """
            SELECT condition_payement_libelle_langue.ref_condition_payement, libelle.libelle
            FROM condition_payement_libelle_langue
            JOIN libelle ON condition_payement_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON condition_payement_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    public List<LabelValue> loadDeliveryConditions(String lang) {
        String sql = """
            SELECT condition_livraison_libelle_langue.ref_mode_de_livraison, libelle.libelle
            FROM condition_livraison_libelle_langue
            JOIN libelle ON condition_livraison_libelle_langue.ref_libelle = libelle.id_libelle
            JOIN langue  ON condition_livraison_libelle_langue.ref_langue  = langue.id_langue
            WHERE langue.key = ?
            ORDER BY libelle.libelle
            """;
        return query(sql, lang);
    }

    // -------------------------------------------------------------------------
    // UTILITAIRES JDBC
    // -------------------------------------------------------------------------

    private List<LabelValue> query(String sql, String lang) {
        List<LabelValue> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lang.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new LabelValue(rs.getString(1), rs.getString(2)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur chargement données dynamiques", e);
        }
        return result;
    }

    private List<LabelValue> queryThreeParams(String sql, String p1, String p2, String p3) {
        List<LabelValue> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p1.toUpperCase());
            ps.setString(2, p2);
            ps.setString(3, p3.toUpperCase());
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