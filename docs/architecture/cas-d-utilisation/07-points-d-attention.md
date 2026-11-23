# Points d'attention

[← Cas d'utilisation](../cas-d-utilisation.md)

Anomalies observées dans le code des vues, classées par impact. Ce sont des constats issus de la lecture du code, non des tests exécutés. Chaque ligne indique la vue ou la méthode concernée.

## Cohérence métier et données

| # | Constat | Où |
|---|---|---|
| 1 | Les compteurs de pagination ne correspondent pas aux listes : `countAPayerByUsername` filtre sur le statut 12 alors que la liste utilise 8 ; `countMesArticlesVendus` utilise 12 contre 14 ; `countAttentePaymentByUsername` compte la table `transaction` alors que la liste lit `a_paye` ; `countALivrerByUsername` sert à quatre vues avec un filtre sur l'acheteur et des statuts « 9 » et « 14 ». Le nombre de pages affiché peut donc être faux. | `MonCompteView`, `ArticleRepository` |
| 2 | `myunsold` (« Mes invendus ») et `myselling` affichent exactement les mêmes données : le filtre de statut est commenté. | `findArticlesByVendeurAndStatut` |
| 3 | Statuts numériques en dur partout (1, 2, 7, 8, 9, 12, 14 ; conditions de paiement 3 et 5 ; modes de livraison 1 à 4). Aucune énumération, aucune table de statuts en base. Le statut 8 désigne à la fois « invendu » (sur `article`) et « en attente de paiement » (sur `a_paye`). | `MonCompteView`, `EnchereScheduler`, `ArticleAuthenticationService` |
| 4 | L'achat direct écrit en dur `ref_statut = 8`, canton 7 et quantité 1, et ignore le paramètre de quantité. | `enregistrerAchat` |
| 5 | Le statut « vendu » est défini de trois façons : `vendu = 1` (catalogue, profil), `quantite = 0` (`EncheresView`) et `ref_statut`. | vues de liste |
| 6 | `confirmerReception` ne conditionne pas la mise à jour au statut et ne vérifie pas que l'utilisateur connecté est l'acheteur : l'identifiant vient de l'objet affiché. | `MonCompteView` |
| 7 | Deux méthodes portent un nom trompeur : `insertAPaye` exécute un `UPDATE`, `showFaireOffreDialog` sert à confirmer un paiement reçu. | `ArticleService`, `MonCompteView` |

## Concurrence et états de l'article

| # | Constat | Où |
|---|---|---|
| 8 | La fiche article ne teste ni `vendu` ni la date de fin d'enchère : les boutons d'offre et d'achat restent actifs sur un article vendu ou une enchère échue. | `DetailArticleView` |
| 9 | Le stock est vérifié sur l'objet chargé à l'arrivée sur la page : deux achats simultanés passent et la quantité en base peut devenir négative. | `showAcheterDialog`, `enregistrerAchat` |
| 10 | Le contrôle « offre supérieure » utilise la dernière offre lue au chargement de la page. Aucune gestion de la concurrence, et la page n'est pas rafraîchie après l'offre. | `showFaireOffreDialog` |
| 11 | Le vendeur peut acheter son propre article et commenter sa propre annonce. Seule l'offre sur son propre article est refusée. | `DetailArticleView` |
| 12 | `EnchereScheduler` clôture une enchère seulement si sa date de fin tombe à la minute courante : une enchère dont la minute passe pendant un arrêt de l'application n'est jamais reprise (voir [diagrammes](../diagrammes.md)). | `EnchereScheduler` |

## Sécurité et accès

| # | Constat | Où |
|---|---|---|
| 13 | Le principal est converti en `OidcUser` sans test, au lieu d'utiliser `AuthenticatedUser`. Un principal d'un autre type ou une session absente provoque une exception. | `MonCompteView`, `EncheresView`, `FairePublicite`, `InformationsPersonnellesView`, `CalendrierView` |
| 14 | Les actions sensibles (confirmer un paiement, une livraison, activer une publicité) ne revérifient pas côté serveur que l'utilisateur est bien le vendeur ou l'acheteur : la protection repose sur le filtrage des listes par e-mail. | `MonCompteView`, `FairePublicite` |
| 15 | Accès anonyme incohérent : l'accueil, les tarifs et la fiche article sont `@PermitAll` alors que la fiche prévoit des messages « connectez-vous » et que le menu est visible des anonymes. `ResultatsRechercheView` n'a aucune annotation. | `MainView`, `TarifView`, `DetailArticleView` |
| 16 | La liste des visiteurs d'un article est accessible à tout utilisateur, et la navigation vers un profil place la partie locale de l'adresse e-mail dans l'URL alors que l'affichage la masque. | `showVisiteursDialog` |
| 17 | Aucun bouton de connexion ni de déconnexion dans le cadre commun. | `MainLayout` |
| 18 | `CustomOAuth2UserService` n'est pas branché explicitement dans `SecurityConfig` : son utilisation dépend de la résolution automatique du bean. | `SecurityConfig` |

## Fonctionnalités incomplètes ou factices

| # | Constat | Où |
|---|---|---|
| 19 | Panneau vidéo par QR code construit mais jamais ajouté à la page (`TODO ADD IT`). | `ArticleFormView` |
| 20 | Vues de maquette ou coquilles : `ResultatsRechercheView`, `ProfilVendeurView`, `NouvelUtilisateurView`, `MesAchatsView`, `WishlistView`, `BoutiqueView`. | voir [index](../cas-d-utilisation.md) |
| 21 | `/test` exposé avec un lien mort. `MonCompteHelper` est du code mort. Plusieurs méthodes, champs et imports sont inutilisés. | `TestUi`, `MonCompteHelper` |
| 22 | Aucun paiement n'est lancé pour la mise en avant, alors que le README cite Stripe. La vue ne montre ni montant ni durée. | `FairePublicite` |
| 23 | Aucun e-mail pour une offre, un achat ou une question ; seule la demande de visite en envoie un. | `DetailArticleView` |

## Validation et saisie

| # | Constat | Où |
|---|---|---|
| 24 | `isValid()` ne contrôle que nom, catégorie et prix. Le fabricant marqué obligatoire n'est pas vérifié, la date de fin d'une enchère peut être nulle. | `DynamicArticleForm` |
| 25 | Les valeurs invalides de la recherche avancée sont ignorées en silence. Le champ « disque dur » est affiché mais pas collecté. « Réinitialiser » ne vide pas les filtres dynamiques. | `CategorieView` |
| 26 | « Réinitialiser » puis « Enregistrer » efface le prénom en base (champ non affiché mais enregistré). | `InformationsPersonnellesView` |
| 27 | La demande de visite n'exige ni nom, ni e-mail, ni téléphone valides, et affiche un succès même si le vendeur est introuvable. | `showDemandeDeVisite` |
| 28 | Les identifiants de catégorie pour Entrupy (1, 2, 34, 35) et la vidéo (1, 2, 35) sont codés en dur et diffèrent d'une catégorie. | `ArticleSubmitService`, `ArticleFormView` |

## Navigation et affichage

| # | Constat | Où |
|---|---|---|
| 29 | Les liens de pagination pointent vers `/{page}` au lieu de `/publicite/{page}` ; le total de pages n'est pas recalculé après une activation. | `FairePublicite` |
| 30 | `CatalogueView` recharge par `findByCategorie(0, 0, 50)` avec un décalage négatif ; vue sans lien d'entrée, doublon de l'accueil. | `CatalogueView` |
| 31 | Aucun lien vers la fiche depuis `EncheresView` ; libellé « enchères » pour une liste d'articles mis en vente. | `EncheresView` |
| 32 | Les liens de profil sont construits de trois façons : par nom d'utilisateur, par « nom prénom » en minuscules, par partie locale de l'e-mail. | `DetailArticleView` |
| 33 | Textes en dur, non traduits : `LoginView`, `CatalogueView`, `NouvelUtilisateurView`, `OtherViews`, `TestUi`, barre de recherche, e-mails de `MonCompteView` (avec des fautes de frappe). | plusieurs vues |
| 34 | Le fichier `ProfilVendeurView.java` déclare un paquet qui ne correspond pas à son dossier ; `EnchereScheduler.java` et `EntrupyWebhookController.java` également. | voir aussi [diagrammes](../diagrammes.md) |
| 35 | Requêtes N+1 : une requête `findDerniereOffre` par ligne (`EncheresView`, `FairePublicite`) et une requête de personne par ligne dans certaines listes de `MonCompteView`. | listes paginées |
| 36 | `MonCompteView` contient un nom de constante étrange, `STATS_PARIS_HILTON`, sans explication : probable donnée de test résiduelle. | `MonCompteView` |

## Priorités suggérées

1. Corriger les compteurs de pagination et clarifier les statuts (points 1, 3) : ils touchent le suivi des transactions.
2. Ajouter des garde-fous sur l'état de l'article et le stock (points 8, 9) et revérifier les droits côté serveur (point 14).
3. Brancher le panneau vidéo ou le retirer (point 19) et renforcer la validation du formulaire de dépôt (point 24).
4. Remplacer les casts `OidcUser` par `AuthenticatedUser` (point 13).
5. Décider du sort des vues factices (points 20, 21).
