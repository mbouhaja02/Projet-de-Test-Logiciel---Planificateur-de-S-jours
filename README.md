# Projet de Test Logiciel - Planificateur de Séjours

## Description
Ce projet, développé dans le cadre du module de test logiciel 2024–2025, vise à appliquer les techniques de tests logiciels. Il permet de planifier des séjours incluant transports, hébergements et activités personnalisées selon les préférences des utilisateurs.

## Fonctionnalités
- **Planification de séjours** : Création de forfaits intégrant transports, hôtels et activités.
- **Critères personnalisés** : Filtrage par prix, durée, catégories d'activités, et distance entre les lieux.
- **Gestion des erreurs** : Génération de forfaits avec rapports d'erreurs en cas de critères non satisfaits.

## Données métiers
- **Transport** : Trajets définis par ville de départ, arrivée, date, heure et mode de transport (train ou avion).
- **Hôtels** : Informations sur l'adresse, le classement (1 à 5 étoiles) et le prix par nuit.
- **Activités** : Catégorie, lieu, date et prix.

## Architecture
L'application est modulaire et comprend des services pour :
- Gestion des transports
- Sélection des hôtels
- Proposition d’activités
- Calcul des distances avec géocodage et formule d’Haversine

Les données sont stockées localement sous format JSON ou CSV.

## Approche de Test
### Tests unitaires
- Vérification des composants isolés avec des doublures de test (stubs et mocks).
### Tests d'intégration
- Validation du comportement global avec des services combinés.
### Couverture de code
- Rapport généré avec JaCoCo.
### Analyse par mutation
- Test de robustesse avec l’outil PIT.

## Prérequis
- **Java** 11 ou supérieur
- **Maven** 3.6+

## Installation et Exécution
1. **Cloner le dépôt** :
   ```bash
   git clone <URL_du_repot>
   cd <nom_du_repot>
   ```
2. **Compilation et exécution** :
   ```bash
   mvn clean install
   mvn exec:java
   ```
3. **Tests** :
   - Unitaires :
     ```bash
     mvn test
     ```
   - Intégration :
     ```bash
     mvn integration-test
     ```
4. **Rapports** :
   - Couverture :
     ```bash
     mvn jacoco:report
     ```
   - Mutation :
     ```bash
     mvn org.pitest:pitest-maven:mutationCoverage
     ```

## Livrables
- **Code source** : Application et tests.
- **Rapports** : Couverture et mutation.
- **Documentation** : Rapport PDF détaillant l'architecture, les tests et les résultats.


