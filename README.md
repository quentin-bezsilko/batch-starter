# Batch Starter - Spring Batch 6 End-to-End Processing Platform

## Overview

Batch Starter est un projet de démonstration construit avec Spring Batch 6 permettant de couvrir l'ensemble du cycle de
traitement de données massives :

- Génération de données bancaires réalistes
- Import CSV massif vers Oracle
- Enrichissement métier
- Détection de fraude
- Reporting
- Gestion des erreurs (Skip / Retry)
- Reprise après incident (Restartability)
- Traitements parallèles et partitionnés
- Observabilité complète avec OpenTelemetry

L'objectif est de démontrer les capacités d'un batch moderne en contexte bancaire avec plusieurs centaines de milliers
d'enregistrements.

---

## Données générées

### Clients

**customers.csv**

| Champ     | Description        |
|-----------|--------------------|
| id        | Identifiant client |
| firstName | Prénom             |
| lastName  | Nom                |
| birthDate | Date de naissance  |
| country   | Pays               |

Volume :

```text
50 000 clients
```

### Comptes bancaires

**accounts.csv**

| Champ         | Description                   |
|---------------|-------------------------------|
| id            | Identifiant du compte         |
| customerId    | Référence client              |
| iban          | IBAN                          |
| accountType   | CHECKING / SAVINGS / BUSINESS |
| currency      | Devise                        |
| balance       | Solde                         |
| accountStatus | ACTIVE / BLOCKED / CLOSED     |
| openingDate   | Date d'ouverture              |

Volume :

```text
~80 000 comptes
```

### Transactions bancaires

**transactions.csv**

| Champ             | Description             |
|-------------------|-------------------------|
| id                | Identifiant transaction |
| accountId         | Référence compte        |
| transactionDate   | Date transaction        |
| transactionType   | Type de transaction     |
| amount            | Montant                 |
| merchantName      | Marchand                |
| currency          | Devise                  |
| transactionStatus | Statut                  |

Volume :

```text
500 000 transactions
```

Répartition :

- 80 % Paiements CB
- 10 % Virements
- 5 % Prélèvements
- 5 % Retraits

---

## Workflow global

```text
Génération des données
          |
          v
Import Clients
          |
          v
Import Comptes
          |
          v
Import Transactions
          |
          v
Enrichissement
          |
          v
Détection de fraude
          |
          v
Reporting
```

---

## Fonctionnalités Spring Batch démontrées

### Chunk Processing

Tous les imports sont réalisés avec :

- ItemReader
- ItemProcessor
- ItemWriter
- Chunk Processing

La taille des chunks est entièrement configurable.

### Import base de données

Les données sont importées via :

```
JdbcBatchItemWriter
```

vers une base Oracle.

### Tolérance aux erreurs

#### Skip

Les enregistrements invalides peuvent être ignorés :

- IBAN invalide
- Données incohérentes
- Erreurs fonctionnelles

#### Retry

Les erreurs temporaires sont automatiquement rejouées :

- Problème réseau
- Erreur technique simulée
- Erreur de base de données transitoire

### Restartabilité

Le projet démontre :

- Sauvegarde des checkpoints
- Relance après incident
- Reprise au dernier état cohérent

Un crash volontaire peut être simulé pour démontrer la reprise automatique.

---

## Enrichissement

Les données sont enrichies lors du traitement.

Exemples :

### Enrichissement Client

- Calcul de l'âge
- Normalisation des noms
- Normalisation des prénoms

### Enrichissement Transaction

- Catégorie métier du marchand
- Pays du marchand
- Niveau de risque

Règles de risque :

| Condition                   | Niveau |
|-----------------------------|--------|
| Montant > 5 000 €           | HIGH   |
| Pays marchand ≠ Pays client | MEDIUM |
| Sinon                       | LOW    |

---

## Détection de fraude

Le batch applique plusieurs règles métier :

### Transaction à montant élevé

```text
Montant > 10 000 €
```

### Retraits répétés

```text
5 retraits en moins de 10 minutes
```

### Incohérence géographique

```text
Transaction dans un pays puis
transaction dans un autre pays
moins de 2 minutes plus tard
```

Les transactions concernées sont remontées dans le rapport de fraude.

---

## Reporting

Les traitements produisent plusieurs rapports :

### Rapport global

- Nombre de transactions
- Montants totaux
- Montants moyens

### Rapport client

- Volumétrie par client
- Dépenses par client

### Rapport compte

- Soldes
- Répartition des statuts

### Rapport fraude

- Transactions suspectes
- Statistiques de risque

---

## Performance

Le projet compare plusieurs stratégies d'exécution.

### Customer Import

```text
Traitement séquentiel
```

### Account Import

```text
TaskExecutor multi-thread
```

### Transaction Import

```text
Partitioning Spring Batch
```

### Exemple de partitionnement

```text
Partition 0 : 1 -> 125000
Partition 1 : 125001 -> 250000
Partition 2 : 250001 -> 375000
Partition 3 : 375001 -> 500000
```

Le partitionnement permet d'exploiter plusieurs workers en parallèle et d'améliorer l'utilisation des ressources.

---

## Observabilité

Une stack complète d'observabilité est intégrée.

### OpenTelemetry

Collecte de :

- Logs
- Métriques
- Traces

### Prometheus

Métriques exposées :

- Temps d'exécution des jobs
- Temps d'exécution des steps
- Nombre de lectures
- Nombre d'écritures
- Nombre de skips
- Nombre de retries

### Grafana

Dashboards :

- Monitoring Batch
- Performance
- KPIs métiers

### Loki

Centralisation des logs :

- Démarrage de job
- Fin de job
- Erreurs
- Skip
- Retry

### Tempo

Visualisation des traces distribuées :

- Exécution du job
- Exécution des steps
- Appels base de données

---

## API REST

### Steps en cours d'exécution

```http
GET /api/batch/status/running
```

Exemple :

```json
[
  "transactionWorkerStep:partition0 - STARTED",
  "transactionWorkerStep:partition1 - STARTED"
]
```

### Historique des exécutions

```http
GET /api/batch/status/history
```

Exemple :

```json
[
  {
    "jobName": "dataWorkflowJob",
    "executionId": 1,
    "status": "COMPLETED"
  }
]
```

---

## Documentation API

Swagger UI :

```text
http://localhost:8080/swagger-ui/index.html
```

Documentation OpenAPI :

```text
http://localhost:8080/v3/api-docs
```

---

## Qualité

### Tests

```text
155 tests unitaires
92.3 % de couverture
```

### SonarQube

```text
Reliability : A
Security : A
Maintainability : A

0 Bugs
0 Vulnerabilities
0 Code Smells
0 Duplication
```

---

## Stack Technique

### Backend

- Java 25
- Spring Boot 4
- Spring Batch 6
- Spring Data JPA
- Hibernate
- Oracle Database

### Tests

- JUnit 5
- Mockito
- AssertJ

### Observabilité

- OpenTelemetry
- Prometheus
- Grafana
- Loki
- Tempo

### Qualité

- SonarQube

---

## Démonstration

Le projet permet de démontrer :

- ✅ Génération de 50 000 clients
- ✅ Génération de 500 000 transactions
- ✅ Import massif vers Oracle
- ✅ Chunk Processing
- ✅ Skip / Retry
- ✅ Reprise après incident
- ✅ Multi-threading
- ✅ Partitioning
- ✅ Monitoring temps réel
- ✅ Métriques Prometheus
- ✅ Dashboards Grafana
- ✅ Logs Loki
- ✅ Traces Tempo
- ✅ Détection de fraude
- ✅ Reporting
- ✅ Qualité logicielle avec SonarQube

---

## Évolutions possibles

- MultiResourcePartitioner
- Remote Partitioning
- Spring Cloud Data Flow
- Planification Kubernetes CronJob
- Ingestion Kafka
- Détection de fraude temps réel
- Notifications d'exécution
- Monitoring SLA

---

Ce projet constitue une démonstration complète d'une architecture Batch moderne basée sur Spring Batch 6, couvrant la
génération de données, les traitements massifs, la résilience, l'observabilité et les bonnes pratiques de qualité
logicielle.