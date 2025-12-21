# Plan

Migrer les tests d'integration Spring Boot (MockMvc + Postgres + Redis) vers des tests Quarkus equivalentes, en conservant les memes endpoints, donnees et assertions, et en s'appuyant sur DevServices pour
Postgres/Redis.

## Requirements
- Conserver des tests HTTP d'integration (pas de tests unitaires).
- Garder l'initialisation des donnees (equivalent de `data-test.sql`).
- Conserver l'usage de Postgres et Redis via DevServices.
- Utiliser `@QuarkusTest` et les outils de test Quarkus.

## Scope
- In: migration des classes de test et de la config de test.
- Out: modification du code metier ou des endpoints.

## Files and entry points
- `src/test/java/ma/aui/sse/it/xcommerce/monolithic/AbstractTestIT.java`
- `src/test/java/ma/aui/sse/it/xcommerce/monolithic/CheckoutTestsIT.java`
- `src/test/java/ma/aui/sse/it/xcommerce/monolithic/ShoppingCartTestsIT.java`
- `src/test/java/ma/aui/sse/it/xcommerce/monolithic/XCommerceMonolithicApplicationTests.java`
- `src/test/resources/application-test.properties`
- `src/test/resources/data-test.sql`
- `src/main/resources/application.properties`

## Data model / API changes
- Aucune.

## Action items
[ ] Definir une config Quarkus de test pour Postgres, Redis et le chargement SQL.
[ ] Remplacer `@SpringBootTest/@AutoConfigureMockMvc` par `@QuarkusTest` et migrer les appels HTTP vers RestAssured.
[ ] Adapter `AbstractTestIT` pour le JSON et le flush Redis via le client Quarkus.
[ ] Migrer les tests concrets (Checkout/ShoppingCart) en conservant les assertions metier.
[ ] Verifier le test de contexte (`contextLoads`) en version Quarkus.

## Testing and validation
- `./mvnw test` (ou `mvn test`) avec DevServices actifs.

## Risks and edge cases
- Differences de configuration Spring -> Quarkus (noms de proprietes, chargement SQL).
- Transactionnalite des tests.
- Demarrage des DevServices (Docker requis).

## Open questions
- Aucun.