# Profils, informations et calendrier

[← Cas d'utilisation](../cas-d-utilisation.md)

Vues couvertes : `InformationsPersonnellesView`, `ProfilView`, `ProfilVendeurView`, `CalendrierView`, `OtherViews` et `TestUi`.

## UC-50 Modifier mes informations personnelles

- **Vue** : `InformationsPersonnellesView` (`/mon-compte/informations`, `@PermitAll`, titre `info.page.title`).
- **Acteur** : utilisateur connecté, propriétaire du compte. **Préconditions** : session OIDC avec e-mail ; ligne `personne` existante (sinon `RuntimeException("Personne non trouvée")`).

**Scénario nominal**
1. La vue charge la personne par e-mail et affiche : nom (lecture seule), pays (liste avec drapeaux, Suisse par défaut), adresse, numéro de rue, ville, NPA, téléphone (indicatif et numéro), IBAN et PayPal.
2. Le changement de pays met à jour l'indicatif téléphonique. La saisie du numéro est formatée au fil de l'eau.
3. Pour PayPal, le préfixe `paypal.me/` est fixe. Si un lien complet est collé, seul le nom de compte est conservé.
4. L'utilisateur clique sur `info.btn.save`.
5. La vue contrôle le NPA (vide = 0 ; non numérique : `info.notif.npa.invalid`) et convertit le téléphone au format E.164 (invalide : `info.notif.phone.invalid`).
6. Elle appelle `personneService.updateProfil` puis affiche `info.notif.saved`.

**Alternatives**
- `info.btn.reset` vide prénom, nom, adresse, ville, NPA et téléphone. Il ne touche ni au numéro de rue, ni à l'IBAN, ni à PayPal, ni au pays, et ne recharge pas les valeurs d'origine.
- Principal non OIDC : `ClassCastException`.

**Postcondition** : ligne `personne` mise à jour. Ces informations (téléphone, IBAN, adresse) servent aux paiements et livraisons de [Mon compte](05-mon-compte-transactions.md).

**Remarques**
- Le champ prénom est créé et enregistré mais jamais ajouté au formulaire. Après « Réinitialiser » (qui le vide), « Enregistrer » efface le prénom en base.
- Aucune validation de l'IBAN.

## UC-51 Consulter le profil d'un utilisateur

- **Vue** : `ProfilView` (`/profil/:username`, `@PermitAll`, titre `profil.pagetitle`).
- **Acteur** : utilisateur connecté (aucun contrôle de propriété : tout utilisateur peut consulter n'importe quel profil).

**Scénario nominal**
1. La vue charge la personne par `personneService.findByNomUtilisateur(username)` et affiche une fiche de statistiques (`findProfilStats`).
2. L'utilisateur clique sur une ligne pour dérouler le détail dans une zone sous la fiche :

| Ligne | Contenu |
|---|---|
| `profil.stat.enchOuv` | enchères ouvertes (`findVentesEncheres`) |
| `profil.stat.ventesOuv`, `ventesFerm` | ventes directes ouvertes ou fermées (`findVentesDirectes`) |
| `profil.stat.evalAchats` | évaluations d'achats avec image, nom, prix et note (`findVEvalAAchatForProfil`) |
| `profil.stat.achetes` | articles achetés (`findArticlesAchetes`) |
| `profil.stat.commentaires` | commentaires avec lien « voir l'article » (`findCommentairesByPersonneId`) |

Les lignes `profil.stat.enchFerm` et `profil.stat.evalVentes` ne sont pas cliquables.

**Alternatives**
- Utilisateur introuvable : message `profil.notfound`.
- Section vide : messages `profil.empty.vente`, `.eval`, `.achete`, `.commentaire`.
- Note non convertible en entier : « — » est affiché.

**Remarques**
- Aucun lien actif du menu ne mène à cette vue. On y arrive depuis la fiche d'un article (nom du vendeur ou d'un enchérisseur).
- La vue contient du code mort : boîte de dialogue des visiteurs, `showEvaluationnVentes`.
- `findProfilStats` reçoit un e-mail dans un paramètre nommé « username » : à vérifier dans le service.

## UC-52 Consulter le profil vendeur (maquette)

- **Vue** : `ProfilVendeurView` (`/vendeur/:username`, `@PermitAll`).

**Comportement réel** : la vue reproduit l'ancienne page CGI `detail_dealer.pl`, mais avec des données factices codées en dur. Quatre sous-vues sont disponibles par le paramètre `?option=` : `list` (derniers visiteurs), `showbuyedarticle`, `evalbuy` et `viewcomments`. Aucun service n'est appelé et un nom d'utilisateur inconnu donne des statistiques à zéro.

**Remarques** : le fichier déclare le paquet `com.avantgarde.views` alors qu'il est rangé dans `com/example/recordz/ui/`. Aucun lien d'entrée. Double emploi apparent avec `ProfilView`, qui est la version réelle.

## UC-53 Consulter mon calendrier

- **Vue** : `CalendrierView` (`/calendrier`, `@PermitAll`, titre `calendrier.pagetitle`).
- **Acteur** : vendeur connecté.

**Scénario nominal**
1. La vue lit l'e-mail OIDC, vérifie que la personne existe, puis construit un calendrier FullCalendar (vue mois par défaut, langue de l'interface).
2. Les événements viennent de `calendrierService.findArticlesByVendeurAndStatut(email)`.
3. L'utilisateur utilise précédent, aujourd'hui, suivant et le sélecteur de vue (mois, semaine, liste).
4. Un clic sur un événement affiche une notification `calendrier.notif.enchere` avec son titre (3 s).

**Alternatives** : personne absente : `RuntimeException` non gérée.

**Remarques** : lecture seule. Un clic n'ouvre pas la fiche de l'article. La vue écrit l'e-mail de l'utilisateur dans la sortie standard (`System.out.println`).

## UC-54 Vues « à venir »

Les quatre classes de `OtherViews` sont déclarées avec `@PermitAll` et le layout principal. Aucun lien du menu n'y mène.

| Vue | Route | Comportement |
|---|---|---|
| `MesAnnoncesView` | `/mes-annonces` | liste les articles du vendeur (`articleService.findByVendeur`) avec nom, marque, prix, statut (« Vendu » si `vendu = 1`) et date ; aucune action |
| `MesAchatsView` | `/mes-achats` | message « Fonctionnalité à venir » ; TODO `CommandeRepository.findByAcheteur()` |
| `WishlistView` | `/wishlist` | message « Fonctionnalité à venir » ; TODO `WishRepository` |
| `BoutiqueView` | `/boutique` | message « Fonctionnalité à venir » ; TODO `BoutiqueRepository` |

Les tables `wish`, `wish_list`, `boutique` et `commande` existent pourtant dans le [modèle de données](../modele-de-donnees.md).

## UC-55 Vue de test

- **Vue** : `TestUi` (`/test`, `@PermitAll`).
- **Comportement** : affiche seulement une barre de liens secondaire (informations, mes enchères, ajouter un article, publicité). Le lien « publicité » pointe vers `#`. Vue de développement exposée en production, avec un titre qui ne correspond pas à son contenu.
