# Rapport des problemes rencontres

## 1) Plan en racine impossible
- Probleme: impossibilite d'ecrire un plan directement dans le repo, contrainte de la skill `plan` (ecriture autorisee uniquement dans `~/.codex/plans`).
- Solution: plan enregistre dans `~/.codex/plans/plan-migration-test.md` et contenu fourni pour copie manuelle.
- Raison: respecter les regles d'execution de la skill.

## 2) `python` introuvable
- Probleme: la commande `python` n'etait pas disponible dans l'environnement.
- Solution: utilisation de `python3`.
- Raison: compatibilite avec l'environnement local.

## 3) Dependence `quarkus-rest-assured` sans version
- Probleme: echec Maven car la version de `quarkus-rest-assured` etait absente.
- Solution: ajout explicite de la version `3.30.4`.
- Raison: le BOM n'etait pas applique a cette dependance, Maven exige une version.

## 4) Conflit Quarkus REST vs RESTEasy
- Probleme: erreur de capabilities entre `quarkus-rest` et `quarkus-resteasy`, et entre `quarkus-rest-jackson` et `quarkus-resteasy-jackson`.
- Solution: suppression de `quarkus-resteasy-jackson`.
- Raison: Quarkus n'accepte qu'un seul stack REST a la fois, et `quarkus-rest` est deja present.

## 5) Chargement SQL en echec (colonnes)
- Probleme: `data.sql`/`data-test.sql` utilisaient `first_name`, `email_address`, `parent_category_id` alors que le schema cree par Hibernate utilisait `firstName`, `emailAddress`, `parentCategory_id`.
- Solution: correction des noms de colonnes dans `src/main/resources/data.sql` pour coller au schema reel.
- Raison: Hibernate derive les noms de colonnes a partir des champs Java (camelCase) sans annotations explicites.

## 6) Contrainte FK produits/categories en echec
- Probleme: violation de cle etrangere parce que les insertions de categories echouaient (mismatch de colonnes), donc `category_id` reference des lignes inexistantes.
- Solution: correction des insertions de categories, ce qui permet ensuite l'insertion des produits.
- Raison: garantir l'ordre logique et l'integrite referentielle.

## 7) `quarkus:dev` bloque par timeout
- Probleme: la commande `mvn quarkus:dev` reste en mode interactif et ne se termine pas, d'ou le timeout.
- Solution: relancer localement et laisser tourner (Ctrl+C pour stopper).
- Raison: comportement normal du mode dev Quarkus.

## 8) Rollback des noms de colonnes via annotations
- Probleme: les noms de colonnes utilises par Hibernate (camelCase) ne correspondaient pas aux colonnes historiques (snake_case), ce qui forçait un ajustement des scripts SQL.
- Solution: ajout d'annotations JPA (`@Column`, `@JoinColumn`) pour mapper explicitement les champs sur les colonnes snake_case, puis remise des noms initiaux dans `data.sql`.
- Raison: conserver la compatibilite avec une base existante sans modifier les scripts SQL historiques.
