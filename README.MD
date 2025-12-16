# XCommerce – Modernisation (branche `legacy-sb-2.1.x`)

## 1. Contexte

Cette branche (`legacy-sb-2.1.x`) traite XCommerce comme un **monolithe legacy Spring Boot 2.1.x** :

- Stack : Spring Boot 2.1.x, PostgreSQL, Redis, Spring Data JPA.
- Objectif : utiliser ce projet comme **terrain d’entraînement** pour moderniser une application existante (stabilisation, sécurisation, refactoring, préparation de migration).

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

