Démo à faire :
- Importer 1.000.000 de lignes d'un CSV vers Postgres (clients, transactions bancaires, commandes e-commerce -> on peut importer plusieurs tables et se créer un vraie BDD)
    - Générer les données et utiliser Reader, Processor, Writer, Chunk optimisés avec logs, BatchJdbcItemWriter
    - Nettoyage et enrichissement des données (ex : calcul d'âge, déduire une info comme le pays ou la catégorie à partir d'un code, déduire un montant de commande, normaliser des données avec capitalize sur nom ou un prénom)
    - Facile de créer plusieurs millions de lignes
- Gestion des erreurs avec Skip et Retry
- Performance (comparaison mono-thread et partitioning + affichage temps de traitement), parallel processing avec TaskExecutor, Partitioning
- Monitoring de l'application (vérifier les temps de traitement, exposer un webservice indiquant où en est le batch (status, nombre de lignes processed et restantes), ajouter des métriques Prometheus et Grafana
- Crash volontaire (montrer la reprise là où le batch en était après incident), montrer le restart automatique (vélocité, résilience)


4 Batches à créer :
- Data generator (générer les CSV et paramétrer le nombre de lignes à créer dans le CSV par fichier)
- Import dans une BDD Postgres / Oracle (Import)
- Enrichissement
- Détection de fraude
  - (amount > 10.000)
  - 5 retraits en moins de 10 minutes
  - Transaction dans un pays puis 2 min après dans un autre
- Reporting (regrouper quelques informations comme les montants par catégorie, le montant journalier, etc).

On gère des transactions bancaires
clients.csv (100.000 enreg, id, firstName, lastName, birthDate, country)
comptes_bancaires.csv (150.000 enreg, id, customerId, iban, AccountType, currency, balance, accountStatus, openingDate)
transactions.csv (5.000.000, id, accountId, transactionDate, transactionType, amount, merchantName, currency, transactionStatus)
  - 80% de paiements CB
  - 10% virements
  - 5% prélèvements
  - 5% retraits

ACCOUNT_TYPE : CHECKING, SAVINGS, BUSINESS
ACCOUNT_STATUS : ACTIVE, BLOCKED, CLOSED
TRANSACTION_TYPE : CardPayment, Transfer, Withdrawal, Deposit, DirectDebit, CashWithdrawal, Deposit
TRANSACTION_STATUS : PENDING, COMPLETED, REJECTED, FRAUD_SUSPECTED
MERCHANT_CATEGORY : Food, Transport, Shopping, Utilities, Entertainment, Other

Enrichir avec les information sur le marchand, la catégorie métier.
Enrichir avec le risque : 
  - amount > 5000 -> RiskLevel High
  - merchantCountry <> accountCountry -> Medium

Roadmap que je suivrais
Sprint 1 (ok)
✅ Domaine
✅ Enums
✅ Référentiels CSV
✅ Service de chargement

Sprint 2 (ok)
✅ Générateur clients
✅ Générateur comptes
✅ Générateur transactions
✅ CSV exportés

Sprint 3 (ok)
✅ Job Spring Batch Generator
✅ Paramétrage nombre de lignes
✅ Logs
✅ Temps de génération

Sprint 4 (ok)
✅ Oracle
✅ Tables SQL
✅ Import BatchJdbcItemWriter
✅ Chunk processing

Sprint 5
✅ Skip
✅ Retry
✅ Crash volontaire
✅ Restart

Sprint 6
✅ Multi-thread
✅ TaskExecutor
✅ Partitioning
✅ Comparaison des performances

Sprint 7
✅ Enrichissement
✅ Détection de fraude
✅ Reporting
✅ Prometheus
✅ Grafana
✅ Endpoint /api/batch/status

Sprint 8 :
✅ Tests unitaires complets
✅ Endpoint /api/batch/status


Plusieurs points importants validés :
✅ Répartition des pays
✅ Chargement des référentiels CSV
✅ Tirage aléatoire pondéré
✅ Association cohérente prénom/nom/pays
✅ Premier batch de génération des CSV OK
✅ Second batch écriture des CSV en BDD OK

TODO :
- Créer tous les TU, plugin Jacoco avant de démarrer sprint 5
- Paramétrer l'application avec un fichier .env
- Reprendre sprint 5
