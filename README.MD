# XCommerce – Modernisation (branche courante)

## 1. Contexte

Cette branche traite XCommerce comme un **monolithe legacy modernise vers Quarkus** :

- Stack : Quarkus 3.x (migration depuis Spring Boot 3.0.x via OpenRewrite), PostgreSQL, Redis, JPA.
- Objectif : utiliser ce projet comme **terrain d’entraînement** pour moderniser une application existante (stabilisation, securisation, refactoring, migration).

---

## 2. État des lieux initial (vue haut niveau)

Avant modernisation, les principaux constats étaient :

- **API & exposition des données**
    - Entités JPA (`@Entity`) exposées directement sur les API REST.
    - Certains DTO (ex. panier) contenaient de la logique métier au lieu de se limiter au transport de données.

- **Logique métier & structure**
    - Règles métier dispersées entre entités, DTO, contrôleurs et services.
    - Packages mélangeant les responsabilités (entités, DTO et repositories dans les mêmes espaces).

- **Testabilité & robustesse**
    - Absence de tests automatisés sur les cas d’usage clés (panier, checkout).
    - Injection par champs et persistance implicite, rendant le code plus difficile à tester et raisonner.

---

## 3. Travaux réalisés

- **Environnement & tests**
    - Ajout d’un `docker-compose.yml` (PostgreSQL + Redis).
    - Mise en place de tests d’intégration avec MockMvc.
    - Migration Spring Boot 3.0 via **OpenRewrite** + correctifs de compatibilité Spring Security.

- **API & sécurité des données**
    - Remplacement de l’exposition directe des entités par des **DTO REST**.
    - Introduction de **MapStruct** pour le mapping DTO ↔ Entity.
    - Ajout d’un `ForbiddenEntitySerializer` pour empêcher la sérialisation des entités JPA dans les réponses API.

- **Logique métier & caractérisation**
    - Déplacement progressif de la logique métier hors des DTO/entités vers des services dédiés.
    - Premiers **tests de caractérisation** autour :
        - des opérations de panier (ajout, retrait, modification de quantité),
        - du use case `/rest/order/checkout` (création de commande, statut, vidage du panier).

- **Structure du projet**
    - Simplification de la structure Maven (POM et sources à la racine du repository).

- **Sécurité (impact Spring Security 3.0)**
    - Par défaut, **toutes les URLs sont sécurisées** en Spring Boot 3.0.
    - Il faut expliciter les routes publiques dans la configuration de sécurité (ex. `permitAll`), sinon elles exigent une authentification.

- **Sécurité (migration vers Quarkus)**
    - Remplacement de la configuration Spring Security par **SmallRye JWT** (generation et verification de tokens).
    - Authentification via `/rest/user/authenticate` et emission d'un JWT base sur les roles stockes en base.
    - Hashing des mots de passe via `BcryptUtil` (Quarkus Elytron).
    - Ajout des cles JWT dans `src/main/resources/privateKey.pem` et `src/main/resources/publicKey.pem`.
    - Configuration JWT dans `src/main/resources/application.properties` (`mp.jwt.verify.*`, `smallrye.jwt.sign.*`).

- **Corrections de build (post-migration Quarkus)**
    - Suppression des dependances et usages Spring Security (classes de config/filter/auditor, `spring-security-test`).
    - Retrait de JJWT et bascule des tokens vers SmallRye JWT.
    - Fix des annotations JAX-RS sans nom (`@QueryParam`, `@PathParam`) dans les controleurs.
    - Nettoyage des references d'auditing Spring (`AuditingEntityListener`) dans `BaseEntity`.
    - Ajout des versions des plugins Maven (`maven-compiler-plugin`, `maven-failsafe-plugin`).

- **Observabilité & logs**
    - Ajout de logs SLF4J et ajustement du niveau de logging à DEBUG pour améliorer la traçabilité

- **Tests & DevServices (Quarkus)**
    - Migration des tests d'integration Spring (MockMvc) vers Quarkus (`@QuarkusTest`) et RestAssured.
    - Activation de DevServices pour PostgreSQL et Redis en test.
    - Mise en place d'une configuration de test Quarkus dans `src/test/resources/application.properties`.

- **Configuration Quarkus**
    - Migration des proprietes Spring vers des proprietes Quarkus dans `src/main/resources/application.properties`.
    - Chargement automatique d'un jeu de donnees au demarrage via `quarkus.hibernate-orm.sql-load-script=data.sql`.

- **Donnees & mapping base**
    - Ajout de `src/main/resources/data.sql` pour initialiser les donnees au demarrage.
    - Ajout d'annotations JPA (`@Column`, `@JoinColumn`) pour aligner les noms de colonnes en base (snake_case).

- **Suivi des incidents**
    - Rapport des problemes et resolutions consigne dans `Issues.md`.
