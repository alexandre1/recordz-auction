# Recordz — Marketplace

Stack : **Java 25 · Vaadin 25 · Spring Boot 3.4 · Spring Security · OAuth2 Google · jOOQ 3.19 · MySQL 9 · Flyway · Docker**

Base de données : schéma **recordz** (marketplace suisse — vêtements, électronique, automobile, vins, enchères…)

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
mvn spring-boot:run
```

→ [http://localhost:8080](http://localhost:8080)

---

## Structure du projet

```
src/main/java/com/example/recordz/
├── RecordzApplication.java
├── config/
│   └── JooqConfig.java                   # DSL settings + @EnableCaching
├── security/
│   ├── SecurityConfig.java               # Vaadin + OAuth2 Google
│   ├── CustomOAuth2UserService.java      # Sync OAuth → personne
│   ├── AuthenticatedUser.java            # Accès utilisateur courant
│   └── LoginView.java                    # Page de connexion Vaadin
├── model/domain/
│   ├── Article.java                      # Annonce marketplace
│   ├── Personne.java                     # Utilisateur / membre
│   ├── Enchere.java                      # Mise aux enchères
│   ├── Commande.java                     # Commande
│   ├── Transaction.java                  # Transaction financière
│   ├── Boutique.java                     # Boutique vendeur
│   ├── Wish.java / WishList.java         # Liste de souhaits
│   ├── Commentaire.java                  # Commentaire article
│   ├── Referentiel.java                  # Tous les types de référence
│   └── Evaluations.java                  # Notes achat/vente/article
├── repository/
│   ├── ArticleRepository.java            # jOOQ — articles
│   ├── PersonneRepository.java           # jOOQ — utilisateurs
│   ├── EnchereRepository.java            # jOOQ — enchères
│   ├── CommandeRepository.java           # jOOQ — commandes
│   └── ReferentielRepository.java        # jOOQ — tables de référence (@Cacheable)
├── service/
│   ├── ArticleService.java               # Logique métier articles
│   ├── EnchereService.java               # Logique enchères avec validation
│   └── PersonneService.java              # Logique utilisateurs
└── ui/
    ├── layouts/MainLayout.java           # Shell + nav drawer
    └── views/
        ├── HomeView.java                 # Accueil
        ├── CatalogueView.java            # Catalogue + recherche
        ├── EncheresView.java             # Enchères actives
        └── OtherViews.java              # MesAnnonces, MesAchats, Wishlist, Boutique, Profil
```

---

## Notes importantes

**OAuth2 ↔ Personne** : à la connexion Google, `CustomOAuth2UserService` fait un upsert dans la table `personne` (email = identifiant unique). Le champ `mot_de_passe` legacy n'est pas utilisé.

**jOOQ code generation** : les repositories utilisent du DSL brut (`table("article")`, `field("nom")`). Après génération, remplacer par les classes typées : `import static com.example.recordz.jooq.tables.Article.ARTICLE`.

**Référentiels mis en cache** : `ReferentielRepository` utilise `@Cacheable` — les cantons, catégories, libellés, etc. sont chargés une fois en mémoire.

**Flyway** : la migration `V1__recordz_schema.sql` recrée tout le schéma en `utf8mb4`. Si vous importez le dump original (`new_db_recordz.sql`) directement, passez `baseline-on-migrate: true` et `baseline-version: 1`.
