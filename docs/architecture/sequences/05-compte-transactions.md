# 05 Compte Transactions

[← Index des diagrammes](./README.md)


## D28 — Charger le tableau de bord Mon compte

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MonCompteView
    participant S as ArticleService / EnchereService
    participant R as ArticleRepository / EnchereRepository
    participant DB as MySQL (a_paye + a_livre + article)
    U->>V: Ouvre /mon-compte
    V->>S: Lit e-mail du principal OIDC
    S->>R: Résout les transactions par jointure personne.email
    R->>DB: Construit le menu des options
    DB-->>R: Charge la liste choisie
    R-->>S: Affiche grille paginée
    opt Variantes et erreurs
        Note over U,V: Principal absent ou non OIDC: traitement non sécurisé à vérifier
        Note over U,V: Listes filtrées par utilisateur
    end
```

**Résultat attendu :** charger le tableau de bord mon compte selon le scénario décrit.

## D29 — Afficher les articles à payer

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_paye + article)
    U->>V: Sélectionne myarticlepayes
    V->>S: Recherche les lignes a_paye statut 8 de cet acheteur
    S->>R: Joint les articles
    R->>DB: Retourne les lignes paginées
    DB-->>R: Affiche action confirmer paiement
    opt Variantes et erreurs
        Note over U,V: Aucune transaction: grille vide
        Note over U,V: L action doit conserver id_a_paye pour cibler une ligne
    end
```

**Résultat attendu :** afficher les articles à payer selon le scénario décrit.

## D30 — Confirmer le paiement par Twint

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView / dialogue paiement
    participant S as ArticleService + QrBillService + e-mail
    participant R as ArticleRepository / PersonneRepository
    participant DB as MySQL (a_paye + a_livre)
    U->>V: Ouvre confirmation
    V->>S: Charge vendeur et téléphone
    S->>R: Calcule montant
    R->>DB: Génère QR bill avec référence Avant-Garde #{id}
    DB-->>R: Affiche les instructions
    R-->>S: Acheteur confirme
    S-->>V: Met a_paye à 7 et crée a_livre statut 8
    V-->>U: Envoie e-mail vendeur
    opt Variantes et erreurs
        Note over U,V: Téléphone vendeur absent: bloque confirmation et demande mise à jour du compte
        Note over U,V: Ligne a_paye absente: IllegalStateException
    end
```

**Résultat attendu :** confirmer le paiement par twint selon le scénario décrit.

## D31 — Confirmer le paiement par virement bancaire

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView / dialogue paiement
    participant S as ArticleService + e-mail
    participant R as ArticleRepository / PersonneRepository
    participant DB as MySQL (a_paye + a_livre)
    U->>V: Ouvre confirmation
    V->>S: Charge IBAN vendeur
    S->>R: Affiche les coordonnées
    R->>DB: Acheteur confirme
    DB-->>R: Met a_paye à 7
    R-->>S: Crée a_livre statut 8 sans doublon
    S-->>V: Envoie e-mail vendeur
    opt Variantes et erreurs
        Note over U,V: IBAN absent: bloque confirmation et envoie une demande de complément au vendeur
    end
```

**Résultat attendu :** confirmer le paiement par virement bancaire selon le scénario décrit.

## D32 — Confirmer un paiement avec autre condition

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView / dialogue paiement
    participant S as ArticleService + e-mail
    participant R as ArticleRepository
    participant DB as MySQL (a_paye + a_livre)
    U->>V: Ouvre confirmation
    V->>S: Affiche article et condition
    S->>R: Acheteur confirme directement
    R->>DB: Met a_paye à 7
    DB-->>R: Crée a_livre statut 8
    R-->>S: Envoie e-mail vendeur
    S-->>V: Recharge la liste
    opt Variantes et erreurs
        Note over U,V: Condition absente: dialogue non ouvert
        Note over U,V: Erreur de mise à jour: pas de succès
    end
```

**Résultat attendu :** confirmer un paiement avec autre condition selon le scénario décrit.

## D33 — Confirmer la réception du paiement

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_paye)
    U->>V: Sélectionne la transaction
    V->>S: Ouvre dialogue reçu
    S->>R: Confirme
    R->>DB: Met à jour acheteur montant date fermeture livraison paiement quantité canton
    DB-->>R: Fixe ref_statut 14
    R-->>S: Recharge liste
    S-->>V: Envoie e-mail
    opt Variantes et erreurs
        Note over U,V: Ligne non liée au vendeur ou statut incompatible: ne pas mettre à jour
        Note over U,V: Contrôle d autorisation à vérifier
    end
```

**Résultat attendu :** confirmer la réception du paiement selon le scénario décrit.

## D34 — Confirmer l expédition

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre)
    U->>V: Ouvre liste à expédier
    V->>S: Sélectionne la transaction
    S->>R: Confirme expédition
    R->>DB: Met a_livre.ref_statut à 9
    DB-->>R: Recharge la liste acheteur et vendeur
    opt Variantes et erreurs
        Note over U,V: Transaction absente ou mauvais statut: mise à jour refusée
        Note over U,V: Notification d erreur
    end
```

**Résultat attendu :** confirmer l expédition selon le scénario décrit.

## D35 — Confirmer la réception par l acheteur

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre)
    U->>V: Ouvre attente de livraison
    V->>S: Sélectionne la ligne
    S->>R: Confirme réception
    R->>DB: Met a_livre.ref_statut à 12
    DB-->>R: Recharge les listes
    opt Variantes et erreurs
        Note over U,V: La mise à jour doit cibler id_a_livre et transaction concernée
        Note over U,V: Ligne absente: erreur
    end
```

**Résultat attendu :** confirmer la réception par l acheteur selon le scénario décrit.

## D36 — Confirmer un retrait sur place

```mermaid
sequenceDiagram
    actor U as Acheteur et vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre)
    U->>V: Ouvre liste de retrait
    V->>S: Sélectionne la transaction
    S->>R: Confirme la remise sur place
    R->>DB: Vérifie le mode retrait 3
    DB-->>R: Met a_livre.ref_statut à 12
    R-->>S: Recharge les listes
    opt Variantes et erreurs
        Note over U,V: Mode de livraison différent de 3: action non applicable
        Note over U,V: Éviter la mise à jour de plusieurs achats
    end
```

**Résultat attendu :** confirmer un retrait sur place selon le scénario décrit.

## D37 — Noter un achat

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView
    participant S as PersonneService / service évaluations
    participant R as Repository évaluations
    participant DB as MySQL (evaluations + a_paye)
    U->>V: Ouvre les achats terminés
    V->>S: Sélectionne un achat
    S->>R: Saisit une note
    R->>DB: Valide
    DB-->>R: Enregistre l évaluation liée à l achat et au vendeur
    R-->>S: Recharge les données
    opt Variantes et erreurs
        Note over U,V: Note invalide ou achat non éligible: refus
        Note over U,V: Nom exact du service à confirmer dans le code
    end
```

**Résultat attendu :** noter un achat selon le scénario décrit.

## D38 — Suivre ventes et achats

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_paye + a_livre + article)
    U->>V: Sélectionne mes achats ou mes ventes
    V->>S: Résout l e-mail connecté
    S->>R: Filtre par acheteur ou vendeur et statut
    R->>DB: Joint les articles
    DB-->>R: Retourne les résultats paginés
    R-->>S: Affiche le suivi
    opt Variantes et erreurs
        Note over U,V: Aucune ligne: liste vide
        Note over U,V: Chaque action dépend du statut et du rôle transactionnel
    end
```

**Résultat attendu :** suivre ventes et achats selon le scénario décrit.

## D39 — Afficher les articles à livrer par poste

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre + article)
    U->>V: Sélectionne myobjectstodeliverByPost
    V->>S: Filtre vendeur statut 8 et mode 1 ou 2
    S->>R: Joint les articles
    R->>DB: Affiche la liste à expédier
    opt Variantes et erreurs
        Note over U,V: Aucune ligne: liste vide
        Note over U,V: Mode domicile/retrait exclu
    end
```

**Résultat attendu :** afficher les articles à livrer par poste selon le scénario décrit.

## D40 — Afficher les livraisons à domicile

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre + article)
    U->>V: Sélectionne myobjectstodeliver
    V->>S: Filtre vendeur statut 8 et mode 4
    S->>R: Joint les articles
    R->>DB: Affiche la liste
    opt Variantes et erreurs
        Note over U,V: Aucune ligne: liste vide
        Note over U,V: Autres modes exclus
    end
```

**Résultat attendu :** afficher les livraisons à domicile selon le scénario décrit.

## D41 — Suivre une livraison postale côté acheteur

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre + article)
    U->>V: Sélectionne mywaitdeliveringbypost
    V->>S: Filtre acheteur statut 9 modes 1 ou 2
    S->>R: Joint les articles
    R->>DB: Affiche les envois attendus
    opt Variantes et erreurs
        Note over U,V: Aucune ligne: liste vide
        Note over U,V: Statut 12 retiré de cette liste
    end
```

**Résultat attendu :** suivre une livraison postale côté acheteur selon le scénario décrit.

## D42 — Suivre une livraison à domicile côté acheteur

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre + article)
    U->>V: Sélectionne mywaitdelivering
    V->>S: Filtre acheteur statut 9 mode 4
    S->>R: Joint les articles
    R->>DB: Affiche les envois attendus
    opt Variantes et erreurs
        Note over U,V: Aucune ligne: liste vide
        Note over U,V: Autres modes exclus
    end
```

**Résultat attendu :** suivre une livraison à domicile côté acheteur selon le scénario décrit.

## D43 — Gérer les articles en attente de retrait

```mermaid
sequenceDiagram
    actor U as Vendeur et acheteur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_livre + a_paye + article)
    U->>V: Vendeur ouvre mywaitpickup ou acheteur myarticletopickup
    V->>S: Filtre transaction par personne statut 8 mode 3
    S->>R: Retourne les articles
    R->>DB: Affiche la confirmation de retrait
    opt Variantes et erreurs
        Note over U,V: Achat multiple: identifier précisément id_a_paye et id_a_livre
        Note over U,V: Ne jamais mettre à jour par seul id_article
    end
```

**Résultat attendu :** gérer les articles en attente de retrait selon le scénario décrit.

## D44 — Consulter les invendus

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article + met_en_vente)
    U->>V: Sélectionne myunsold
    V->>S: Résout le vendeur
    S->>R: Recherche ses articles non vendus
    R->>DB: Retourne la page
    DB-->>R: Affiche les annonces
    opt Variantes et erreurs
        Note over U,V: Aucune annonce: liste vide
    end
```

**Résultat attendu :** consulter les invendus selon le scénario décrit.

## D45 — Consulter les objets en vente

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article + met_en_vente)
    U->>V: Sélectionne myselling
    V->>S: Filtre les annonces actives du vendeur
    S->>R: Retourne les articles
    R->>DB: Affiche la liste paginée
    opt Variantes et erreurs
        Note over U,V: Aucune annonce: liste vide
    end
```

**Résultat attendu :** consulter les objets en vente selon le scénario décrit.

## D46 — Consulter les achats clôturés

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_paye + article)
    U->>V: Sélectionne mybought
    V->>S: Filtre a_paye statut 14 par acheteur
    S->>R: Joint article
    R->>DB: Affiche les achats terminés
    opt Variantes et erreurs
        Note over U,V: Aucune transaction: liste vide
    end
```

**Résultat attendu :** consulter les achats clôturés selon le scénario décrit.

## D47 — Consulter les ventes clôturées

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (a_paye + article)
    U->>V: Sélectionne mysold
    V->>S: Filtre a_paye statut 14 par vendeur
    S->>R: Joint article
    R->>DB: Affiche les ventes terminées
    opt Variantes et erreurs
        Note over U,V: Aucune transaction: liste vide
    end
```

**Résultat attendu :** consulter les ventes clôturées selon le scénario décrit.

## D48 — Consulter les statistiques par marque

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MonCompteView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article + marque)
    U->>V: Sélectionne statdealarticlebrand
    V->>S: Demande les agrégats par marque
    S->>R: Exécute le regroupement
    R->>DB: Retourne les statistiques
    DB-->>R: Affiche les résultats
    opt Variantes et erreurs
        Note over U,V: Données absentes: statistiques vides
    end
```

**Résultat attendu :** consulter les statistiques par marque selon le scénario décrit.

## D49 — Consulter ses enchères et leurs résultats

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as EncheresView
    participant S as EnchereService
    participant R as EnchereRepository
    participant DB as MySQL (enchere + article)
    U->>V: Ouvre /encheres
    V->>S: Résout l identité du compte
    S->>R: Charge les offres émises
    R->>DB: Joint les informations article et statut
    DB-->>R: Affiche la pagination
    opt Variantes et erreurs
        Note over U,V: Aucune enchère: liste vide
        Note over U,V: Route page invalide: page corrigée
    end
```

**Résultat attendu :** consulter ses enchères et leurs résultats selon le scénario décrit.

## D50 — Clôturer automatiquement une enchère arrivée à échéance

```mermaid
sequenceDiagram
    actor U as Système scheduler
    participant V as EnchereScheduler
    participant S as EnchereService
    participant R as EnchereRepository / ArticleRepository
    participant DB as MySQL (enchere + a_paye)
    U->>V: Déclenche le scheduler périodique
    V->>S: Recherche les enchères expirées
    S->>R: Détermine l offre gagnante
    R->>DB: Clôture l enchère
    DB-->>R: Crée la transaction a_paye en attente statut 8
    R-->>S: Déclenche les notifications prévues
    opt Variantes et erreurs
        Note over U,V: Aucune offre: traitement de l enchère sans gagnant
        Note over U,V: Erreur transaction: journalisation et reprise à définir
    end
```

**Résultat attendu :** clôturer automatiquement une enchère arrivée à échéance selon le scénario décrit.
