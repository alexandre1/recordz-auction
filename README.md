# Recordz — Marketplace

Cette application Web Java Full Stack représente une place de marché Suisse permettant aux vendeurs et aux acheteurs 
d'acheter et de vendre des articles de luxe sans frais de transaction. Aucun coût n'est facturé lors de la mise en vente
ou de l'achat d'un article.

Le financement se fera par la mise en évidence des articles sur les pages principales.

Stack : **Java 25 · Vaadin 25 · Spring Boot 3.4 · Spring Security · OAuth2 Google · jOOQ 3.19 · MySQL 9 · Flyway · Docker**

Base de données : schéma **recordz** (marketplace suisse — vêtements, électronique, automobile, vins, enchères…)

https://github.com/user-attachments/assets/3d7bcc19-9083-4080-a51e-5fcf0e230eb3

https://github.com/user-attachments/assets/502e66e0-90f8-4fb0-b37f-e65362d9bcac

https://github.com/user-attachments/assets/e759f9c3-66ef-4efb-99f5-e367ad3b2620

---

## Architecture du schéma (résumé)

| Groupe | Tables clés |
|---|---|
| Catalogue | `article`, `met_en_vente`, `genre`, `categorie`, `main_categorie`, `subcategorie` |
| Utilisateurs | `personne`, `boutique`, `visiteur`, `sessions` |
| Enchères | `enchere`, `a_paye`, `a_livre` |
| Commandes | `commande`, `commande_article`, `transaction` |
| Évaluations | `evaluation_achat`, `evaluation_article`, `evaluation_vente` |
| Référentiels | `libelle`, `langue`, `canton_fr`, `departement`, `pays_present`, `pays_region_vin`, `cepage` |
| Souhaits | `wish`, `wish_list` |
| Divers | `commentaire`, `demande_visite`, `recherche`, `depot`, `article_ip`, `article_visite` |

---

## Démarrage rapide

```bash
# 1. Variables d'environnement
cp .env.example .env
# Remplir GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, DB_*

# 2. Démarrer MySQL
docker compose up mysql -d

# 3. Générer le code jOOQ (après que Flyway ait créé les tables)
mvn generate-sources \
  -Djooq.codegen.url=jdbc:mysql://localhost:3306/new_db_recordz \
  -Djooq.codegen.user=root \
  -Djooq.codegen.password= \
  -Djooq.codegen.schema=new_db_recordz

# 4. Lancer l'application
export GOOGLE_CLIENT_ID=...
export GOOGLE_CLIENT_SECRET=...
mvn spring-boot:run -pl recordz-web
```

→ [http://localhost:8081](http://localhost:8081)

---

## Structure du projet