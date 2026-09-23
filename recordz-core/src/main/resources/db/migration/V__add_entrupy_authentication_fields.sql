-- Renommez ce fichier selon votre convention Flyway existante
-- (ex: V27__add_entrupy_authentication_fields.sql)

ALTER TABLE article
    ADD COLUMN entrupy_customer_item_id VARCHAR(64)  NULL,
    ADD COLUMN entrupy_status           VARCHAR(32)  NULL,   -- pending / authentic / unidentified / invalid / not_supported
    ADD COLUMN entrupy_id               VARCHAR(64)  NULL,   -- id Entrupy de l'authentification
    ADD COLUMN entrupy_certificate_url  VARCHAR(255) NULL,
    ADD COLUMN entrupy_updated_at       TIMESTAMP    NULL;

CREATE UNIQUE INDEX ux_article_entrupy_customer_item_id
    ON article (entrupy_customer_item_id);
