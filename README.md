# Recordz — Marketplace

Cette application Web Java Full Stack représente une place de marché Suisse permettant aux vendeurs et aux acheteurs
d'acheter et de vendre des articles de luxe sans frais de transaction. Aucun coût n'est facturé lors de la mise en vente
ou de l'achat d'un article.

Le financement se fera par la mise en évidence des articles sur les pages principales.

Stack : **Java 21 · Vaadin 25.0.6 · Spring Boot 4.0.1 · Spring Security · OAuth2 Google · jOOQ 3.20.11 · MySQL / MariaDB · Flyway · Docker · Stripe**

Base de données : schéma **`new_db_recordzv3`** (marketplace suisse — vêtements, électronique, automobile, immobilier, vins, jeux vidéo, instruments de musique, enchères…). Migration Flyway de référence : `recordz-core/src/main/resources/db/migration/new_db_recordzv3.sql`.

https://github.com/user-attachments/assets/3d7bcc19-9083-4080-a51e-5fcf0e230eb3

https://github.com/user-attachments/assets/502e66e0-90f8-4fb0-b37f-e65362d9bcac

https://github.com/user-attachments/assets/e759f9c3-66ef-4efb-99f5-e367ad3b2620

---

## Architecture du schéma (résumé)

Le schéma `new_db_recordzv3` est fortement dénormalisé sur la table `article` : une seule table plate porte tous les attributs possibles (vêtements, automobile, immobilier, vins, jeux vidéo, informatique, etc.), remplis ou non selon la catégorie. Les libellés multilingues passent systématiquement par des tables `xxx_libelle_langue` qui font le lien vers la table `libelle` commune.

| Groupe | Tables clés | Rôle |
|---|---|---|
| Catalogue | `article`, `met_en_vente`, `article_ip`, `article_visite` | Annonce plate multi-catégories + mise en vente par vendeur + tracking visites/IP |
| Cycle de vente | `enchere`, `a_paye`, `a_livre`, `transaction`, `commande`, `commande_article` | Enchères → paiement (`a_paye`) → livraison (`a_livre`) ; `transaction`/`commande` legacy |
| Utilisateurs | `personne`, `app_user`, `boutique`, `boutique_a_categorie`, `visiteur`, `sessions` | Comptes (email unique), boutiques vendeur, sessions PHP legacy |
| Souhaits & interactions | `wish`, `wish_list`, `commentaire`, `demande_visite`, `recherche` | Liste de souhaits, Q&A sur annonce, demandes de visite, recherches sauvegardées |
| Évaluations | `evaluation_achat`, `evaluation_article`, `evaluation_vente` | Notes/commentaires acheteur ↔ vendeur ↔ article |
| Logistique | `depot`, `condition_livraison`, `condition_livraison_libelle_langue`, `condition_payement_libelle_langue` | Dépôts physiques, modes de livraison/paiement et leurs frais |
| Catégorisation | `categorie_libelle_langue`, `subcategorie_libelle_langue`, `main_categorie_libelle_langue`, `genre` | Arborescence catégorie → sous-catégorie, tout en `_libelle_langue` |
| Référentiels génériques | `libelle`, `langue`, `canton_fr`, `pays`, `pays_present`, `departement`, `etat`, `taille_libelle_langue`, `pointure`, `states` | Traductions FR/DE, géographie CH/FR, tailles/pointures |
| Automobile | `boite_de_vitesse(_libelle_langue)`, `type_essence(_libelle_langue)` | Boîte de vitesse, type de motorisation |
| Vin | `cepage`, `pays_region_vin`, `type_de_vin(_libelle_langue)` | Cépage, région/pays, type de vin |
| Jeux / écrans | `type_de_jeux(_libelle_langue)`, `type_ecran(_libelle_langue)` | Genre de jeu vidéo, type d'écran |
| Immobilier | `location_ou_achat_libelle_langue` | Location vs achat |
| Comptes/paiement | `type_de_compte(_libelle_langue)`, `mode_de_payement(_libelle_langue)`, `publication_option(_libelle_langue)` | Types de compte, options de mise en avant payante |
| Divers / temps | `mois(_libelle_langue)`, `temps(_libelle_langue)` | Référentiels calendaires legacy |

**Points d'attention connus du schéma** (visibles dans le dump et gérés explicitement côté code, cf. `ArticleRepository`) :
- Les colonnes `date`, `enchere_date_debut`, `enchere_date_fin` sont typées `varchar`, pas `datetime` — le repository les caste et neutralise les valeurs `'0000-00-00 00:00:00'` (`NULLIF(CAST(... AS CHAR), '0000-00-00 ...')`) pour éviter l'erreur MariaDB *"Zero date value prohibited"*.
- Plusieurs colonnes de la table `libelle` référencées en clé primaire composite ont des doublons de `ref_libelle` pour un même `ref_categorie` (ex. `categorie_libelle_langue`), à garder en tête lors de jointures strictes.
- `article.montant`/`prix_achat` mélangent `double` et `decimal` selon les tables (`a_livre.montant` est `varchar` !) — les conversions sont faites manuellement dans les mappers (`toArticle`, requêtes `findArticlesAPaye`...).

---

## Démarrage rapide

```bash
# 1. Variables d'environnement
cp .env.example .env
# Remplir GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, DB_*

# 2. Démarrer MySQL
docker compose up mysql -d

# 3. Générer le code jOOQ (après que Flyway ait créé les tables) — profil "codegen"
mvn generate-sources -P codegen -pl recordz-core \
  -Djooq.codegen.url=jdbc:mysql://localhost:3306/new_db_recordzv3 \
  -Djooq.codegen.user=root \
  -Djooq.codegen.password=


(Ou alors importer le fichier .sql dans PhpMyAdmin sans passer par l'étape numéro 3)

# 4. Lancer l'application
export GOOGLE_CLIENT_ID=...
export GOOGLE_CLIENT_SECRET=...
mvn spring-boot:run -pl recordz-web
```

→ [http://localhost:8081](http://localhost:8081)

---

## Structure du projet

```
recordz-core/                                      # Logique métier — indépendant de Vaadin/Web
└── src/main/java/com/example/recordz/
    ├── config/
    │   └── JooqConfig.java                         # DSL settings + @EnableCaching
    ├── model/domain/
    │   ├── Article.java                            # Annonce marketplace (table plate multi-catégories)
    │   ├── Personne.java                           # Utilisateur / membre
    │   ├── Enchere.java                             # Mise aux enchères
    │   ├── Commande.java / Transaction.java         # Commande & transaction financière (legacy)
    │   ├── Boutique.java                            # Boutique vendeur
    │   ├── Wish.java / WishList.java                # Liste de souhaits
    │   ├── Commentaire.java                         # Q&A sur une annonce
    │   ├── Evaluations.java                          # Notes achat/vente/article
    │   ├── FiltreArticle.java                        # Objet de filtres dynamiques (recherche/catalogue)
    │   ├── ArticleCategory.java / ArticleSubCategory.java  # IDs de catégories/sous-catégories
    │   ├── EvenementCalendrier.java                  # Événement du calendrier vendeur
    │   ├── Referentiel.java                          # Agrégat de tous les référentiels (cantons, tailles...)
    │   └── dto/ArticleFormData.java                  # DTO du formulaire de dépôt d'annonce
    ├── repository/                                   # jOOQ DSL brut (table()/field())
    │   ├── ArticleRepository.java                    # Articles : recherche, filtres, cycle vente/livraison
    │   ├── PersonneRepository.java                   # Utilisateurs
    │   ├── EnchereRepository.java                    # Enchères
    │   ├── CommandeRepository.java                   # Commandes (legacy)
    │   └── ReferentielRepository.java                # Tables de référence, @Cacheable
    └── service/
        ├── ArticleService.java                       # Logique métier articles (lecture/recherche)
        ├── ArticleDynamicDataService.java            # Résolution des champs dynamiques par catégorie
        ├── ArticleSubmitService.java                 # Validation + dépôt d'une nouvelle annonce
        ├── EnchereService.java                       # Logique enchères avec validation
        ├── PersonneService.java                      # Logique utilisateurs
        ├── CalendrierService.java                    # Événements du calendrier vendeur
        └── ReferenceService.java                     # Résolution des libellés/référentiels par langue

recordz-web/                                        # Point d'entrée Spring Boot + UI Vaadin
└── src/main/java/com/example/recordz/
    ├── RecordzApplication.java
    ├── EnchereScheduler.java                         # Job planifié : clôture des enchères expirées
    ├── config/
    │   └── WebMvcConfig.java
    ├── security/
    │   ├── SecurityConfig.java                       # Vaadin + OAuth2 Google
    │   ├── CustomOAuth2UserService.java              # Sync OAuth → personne (upsert par email)
    │   └── AuthenticatedUser.java                    # Accès à l'utilisateur courant depuis les vues
    └── ui/
        ├── layouts/MainLayout.java                   # Shell + nav drawer
        ├── LoginView.java / NouvelUtilisateurView.java     # Connexion & inscription
        ├── MainView.java / CatalogueView.java / CategorieView.java  # Accueil, catalogue, navigation catégories
        ├── ResultatsRechercheView.java                # Résultats de recherche
        ├── EncheresView.java                          # Enchères actives
        ├── ArticleFormView.java / DynamicArticleForm.java   # Dépôt d'annonce (formulaire dynamique par catégorie)
        ├── DetailArticleView.java                     # Fiche détail d'une annonce
        ├── FairePublicite.java / TarifView.java       # Mise en avant payante d'une annonce (Stripe)
        ├── MonCompteView.java / MonCompteHelper.java / InformationsPersonnellesView.java   # Espace membre
        ├── ProfilView.java / ProfilVendeurView.java   # Profil public / profil vendeur
        ├── CalendrierView.java                        # Calendrier vendeur
        ├── AProposView.java / OtherViews.java / TestUi.java  # Pages annexes et vues de test
        └── StyleHelper.java                           # Utilitaires de style Vaadin partagés
```

---

## Architecture logicielle

### Modules Maven

Le projet est découpé en deux modules pour séparer la logique métier de la couche web :

- **`recordz-core`** — modèles de domaine, repositories (accès données) et services (logique métier). Indépendant de Vaadin/Spring Web, réutilisable si une autre interface (API REST, batch) devait un jour consommer la même logique.
- **`recordz-web`** — point d'entrée Spring Boot, configuration, sécurité et interface utilisateur Vaadin. Dépend de `recordz-core`.

### Couches applicatives

L'application suit une architecture en couches classique :

```
┌─────────────────────────────────────┐
│   UI (Vaadin Views)                  │  recordz-web/ui
│   MainView, CatalogueView, ...       │
└──────────────┬────────────────────────┘
               │
┌──────────────▼────────────────────────┐
│   Service (logique métier)           │  recordz-core/service
│   ArticleService, EnchereService...  │
│   @Transactional                     │
└──────────────┬────────────────────────┘
               │
┌──────────────▼────────────────────────┐
│   Repository (accès données)         │  recordz-core/repository
│   DSL jOOQ brut                      │
└──────────────┬────────────────────────┘
               │
┌──────────────▼────────────────────────┐
│   MySQL/MariaDB (new_db_recordzv3)   │  via HikariCP
└─────────────────────────────────────┘
```

Chaque couche ne dépend que de la couche immédiatement inférieure : les vues Vaadin n'appellent jamais un repository directement, elles passent systématiquement par un service.

### Sécurité et authentification

L'authentification repose entièrement sur **OAuth2 Google** (pas de mot de passe local) :

1. `SecurityConfig` délègue la connexion à `VaadinSecurityConfigurer` + `oauth2Login`.
2. À chaque connexion, `CustomOAuth2UserService` intercepte le flux OIDC et fait un **upsert** dans la table `personne` (email = identifiant unique). Le champ `mot_de_passe` legacy n'est pas utilisé.
3. `AuthenticatedUser` expose ensuite l'utilisateur courant aux vues Vaadin sans qu'elles aient à connaître les détails OAuth2.

### Accès aux données

- **jOOQ en DSL brut** (`table("article")`, `field("nom")`) dans tous les repositories actuels, tant que la génération de code typée n'a pas encore été branchée en continu. La génération typée existe déjà comme profil Maven désactivé par défaut (`phase: none`) et s'active avec `mvn generate-sources -P codegen -pl recordz-core` (schéma cible : `new_db_recordz`). Une fois activée, remplacer progressivement par les classes générées : `import static com.example.recordz.jooq.tables.Article.ARTICLE`.
- **HikariCP** comme pool de connexions (`maximum-pool-size: 10`, `minimum-idle: 2`), configuré dans `application.yml` et `JooqConfig`.
- **Flyway** pilote le schéma via `recordz-core/src/main/resources/db/migration/new_db_recordzv3.sql`, chargée en `baseline-on-migrate: true` / `baseline-version: 0` (le schéma existant sert de point de départ). Toute évolution du schéma doit passer par une nouvelle migration versionnée, jamais par une modification de ce fichier.
- **Concurrence** : les opérations sensibles à la concurrence (ex. upsert OAuth2 dans `PersonneRepository`) s'appuient sur les contraintes `UNIQUE` en base (`personne.email`, `personne.nom_utilisateur`) plutôt que sur des vérifications applicatives, pour rester correctes sous forte charge concurrente.
- **Paiement** : intégration Stripe (`stripe-java`) pour la mise en avant payante des annonces (`FairePublicite`, `TarifView`).

### Caching

`ReferentielRepository` utilise `@Cacheable` pour les données de référence peu volatiles (cantons, catégories, libellés, etc.) — chargées une fois en mémoire au lieu d'un aller-retour DB à chaque affichage.