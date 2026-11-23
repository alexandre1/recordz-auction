# Diagrammes de séquence UML — Recordz

> **Périmètre : 55 diagrammes détaillés** dérivés des cas d’utilisation publiés dans `docs/architecture/cas-d-utilisation/` et de l’architecture décrite dans `docs/architecture/diagrammes.md`.

## Convention de lecture

- `Utilisateur` représente l’acteur humain (visiteur, utilisateur connecté, acheteur ou vendeur selon le scénario).
- `Vue Vaadin` représente la vue/composant UI indiqué dans chaque diagramme.
- `Service` et `Repository` sont les couches métier et d’accès aux données.
- `MySQL` représente le schéma `new_db_recordzv3`.
- Les interactions externes (Google, Entrupy, SMTP) ne sont montrées que lorsqu’elles sont décrites par la documentation.
- Les diagrammes décrivent l’existant documenté, pas un comportement cible. Les opérations signalées comme maquettes, prototypes ou « à vérifier » ne doivent pas être interprétées comme fonctionnelles.
- Les noms de méthodes de repository sont volontairement génériques lorsqu’ils ne sont pas cités explicitement dans les documents.

## Index

- [01-acces-navigation.md](./01-acces-navigation.md) — 7 diagrammes
- [02-catalogue-recherche.md](./02-catalogue-recherche.md) — 6 diagrammes
- [03-fiche-article-offres.md](./03-fiche-article-offres.md) — 7 diagrammes
- [04-publication-promotion.md](./04-publication-promotion.md) — 7 diagrammes
- [05-compte-transactions.md](./05-compte-transactions.md) — 23 diagrammes
- [06-profils-calendrier-maquettes.md](./06-profils-calendrier-maquettes.md) — 5 diagrammes

## Références

- [Cas d’utilisation — index](../cas-d-utilisation.md)
- [Architecture et composants](../diagrammes.md)
- [Modèle de données](../modele-de-donnees.md)

