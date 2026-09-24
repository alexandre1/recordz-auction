package com.example.recordz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class ArticleAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(ArticleAuthenticationService.class);

    private final DataSource dataSource;

    public ArticleAuthenticationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param customerItemId  ex. "CHIC-42", reçu dans le payload webhook
     * @param entrupyId       identifiant Entrupy de la session
     * @param certificateUrl  lien du certificat (peut être null si pas encore dispo)
     * @param isAuthentic     résultat de l'authentification
     */
    public void updateAuthenticationStatus(String customerItemId,
                                           String entrupyId,
                                           String certificateUrl,
                                           boolean isAuthentic) {

        String newStatus = isAuthentic ? "authentic" : "not_authentic";

        String sql = """
            UPDATE article
            SET entrupy_status = ?,
                entrupy_id = ?,
                entrupy_certificate_url = ?,
                entrupy_updated_at = NOW()
            WHERE entrupy_customer_item_id = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, entrupyId);
            ps.setString(3, certificateUrl);
            ps.setString(4, customerItemId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                log.warn("Aucun article trouvé pour customer_item_id={} — webhook ignoré", customerItemId);
                return;
            }

            log.info("Article mis à jour : customer_item_id={}, entrupy_status={}", customerItemId, newStatus);

            if (isAuthentic) {
                publishArticle(conn, customerItemId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur mise à jour statut Entrupy pour " + customerItemId, e);
        }
    }

    /**
     * Rend l'article visible aux acheteurs une fois l'authenticité confirmée.
     * TODO : remplacer 1 par le véritable id ref_statut "publié / actif"
     * dans votre table ref_statut (à confirmer).
     */
    private void publishArticle(Connection conn, String customerItemId) throws SQLException {
        String sql = "UPDATE article SET ref_statut = ? WHERE entrupy_customer_item_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, /* STATUT_PUBLISHED à définir */ 2);
            ps.setString(2, customerItemId);
            ps.executeUpdate();
        }
    }
}