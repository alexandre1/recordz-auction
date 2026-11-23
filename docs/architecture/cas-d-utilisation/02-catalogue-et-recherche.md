# Catalogue et recherche

[← Cas d'utilisation](../cas-d-utilisation.md)

Vues couvertes : `CategorieView`, `CatalogueView`, `ResultatsRechercheView`.

## UC-10 Parcourir les annonces par catégorie

- **Vue** : `CategorieView` (`/categorie/{id}`, `@AnonymousAllowed`). L'id 0 désigne « toutes catégories ».
- **Acteur** : tout visiteur.
- **Paramètres de requête** : `page` (défaut 1, valeur invalide ramenée à 1) et `nom` (filtre de nom).

**Scénario nominal**
1. L'utilisateur arrive depuis le tiroir des catégories ou la barre de recherche ([UC-03](01-acces-et-navigation.md)).
2. La vue affiche deux sélecteurs (catégorie, sous-catégorie) et un tableau de 10 résultats par page avec image, nom (lien), marque et prix.
3. Il change de catégorie : la sous-catégorie est remise à zéro, la liste des sous-catégories est rechargée (`findSousCategoriesByCategorie`) et les résultats reviennent en page 1.
4. Il choisit une sous-catégorie : les résultats sont filtrés et reviennent en page 1.
5. Il navigue avec la pagination (liens vers `/categorie/{id}?page=N`).
6. Il clique sur une ligne ou un nom : navigation vers `detail/{id}` ([UC-20](03-fiche-article-et-offres.md)).

**Alternatives et erreurs**
- Aucun résultat : message `main.empty`.
- Catégorie inconnue dans l'URL : le sélecteur n'est pas mis à jour mais le filtre est quand même appliqué.
- Aucun `try/catch` autour des appels de service.

**Postconditions** : lecture seule, aucun changement en base.

**Remarques**
- Le paramètre d'id est obligatoire : `/categorie` sans id n'est pas géré.
- Le clic de ligne et le lien du nom peuvent provoquer une double navigation.

## UC-11 Recherche avancée

- **Vue** : `CategorieView`, dialogue ouvert par le bouton `cat.adv.title`.
- **Acteur** : tout visiteur.

**Champs du dialogue** : nom, marque, catégorie, sous-catégorie, prix minimum et maximum, case « enchères seulement », plus des champs dynamiques.

| Contexte | Champs dynamiques |
|---|---|
| Voiture, Moto | km max, année min et max, chevaux min, carburant, boîte de vitesses |
| Immobilier | pièces min, surface min et max, lieu, NPA |
| Vins | millésime min et max, type de vin |
| DVD | réalisateur, acteurs, année min et max |
| Livres | auteur, éditeur, année min |
| Jeux | type de jeu |
| Sous-catégorie TV écran plat | dimension min et max, type d'écran |
| Sous-catégories PC | RAM, processeur, disque dur |
| Sous-catégories vêtements | taille |

**Scénario nominal**
1. L'utilisateur ouvre le dialogue, renseigne des critères et clique sur « Rechercher ».
2. Le handler copie les critères dans le filtre (`FiltreArticle`), lit les champs dynamiques (`collectDynamicSearchFilters`), repasse en page 1 et appelle `findByFiltresDynamiques` et `countByFiltresDynamiques`.
3. Le dialogue se ferme et la liste est remplacée.

**Alternatives**
- « Réinitialiser » remet à zéro nom, marque, prix, enchères et sous-catégorie, puis recharge. Il ne réinitialise ni la catégorie ni les filtres dynamiques.
- « Annuler » ferme le dialogue sans changement.
- Une valeur numérique invalide est ignorée sans message (elle devient `null`).

**Remarques**
- Les sélecteurs de catégorie du dialogue ne sont pas préremplis. Valider sans catégorie efface la catégorie courante du filtre sans mettre à jour le sélecteur de la page.
- Le champ « disque dur » est affiché mais n'est pas pris en compte dans la collecte des filtres.
- Les anciens filtres dynamiques ne sont jamais vidés quand on change de catégorie.

## UC-12 Consulter le catalogue par mot-clé

- **Vue** : `CatalogueView` (`/catalogue`, `@PermitAll`, titre non traduit).
- **Acteur** : utilisateur connecté.

**Scénario nominal**
1. La vue charge les 50 premiers articles (`findAll(1, 50)`).
2. L'utilisateur saisit un mot-clé et clique sur « Rechercher ».
3. `articleService.search(valeur)` remplace la liste (50 résultats au plus). Un mot-clé vide donne une liste vide.
4. Le tableau affiche nom, marque, prix, état, langue, statut (« Vendu » si `vendu = 1`, sinon « Disponible ») et visites.

**Alternatives**
- Vider le champ recharge la liste par `findByCategorie(0, 0, 50)`. Avec la page 0, le décalage calculé est négatif : le résultat n'est pas vérifié.
- La touche Entrée n'est pas gérée.

**Remarques** : aucun lien vers la fiche d'article, pas de pagination, textes en dur. Aucun lien d'entrée dans le menu : cette vue fait doublon avec l'accueil et la recherche par catégorie.

## UC-13 Afficher des résultats de recherche (maquette)

- **Vue** : `ResultatsRechercheView` (`/recherche`, aucune annotation d'accès).

**Comportement réel** : la vue affiche quatre articles Chanel codés en dur (ids 10 à 13) avec un lien vers `/detail/{id}`. Aucune recherche, aucun service. Les liens de pagination pointent vers `#`.

**Remarque** : vue de prototype, non reliée au reste de l'application. Les ids 10 à 13 peuvent ne pas exister en base, auquel cas la fiche affiche `detail.notfound`.
