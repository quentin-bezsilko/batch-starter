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

Sprint 5 (ok)
✅ Skip
✅ Retry
✅ Crash volontaire
✅ Restart / Reprise

Sprint 6 (ok)
✅ Multi-thread
✅ TaskExecutor
✅ Partitioning
✅ Comparaison des performances

Au final on laissera comme ça pour une démo complète : 
CustomerImportStep      -> 100.000 (Séquentiel)
AccountImportStep       -> 300.000 (Multi-thread / TaskExecutor)
TransactionImportStep   -> 1.000.000 (Partitioning)
=> Supprimer les profiles

DEFAULT :
2026-10-29T14:26:38.097Z  INFO 1 --- [batchstarter] [           main] o.s.b.b.a.JobLauncherApplicationRunner   : Running default command line with: []
2026-10-29T14:26:38.261Z  INFO 1 --- [batchstarter] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=dataWorkflowJob]] launched with the following parameters: [{}]
2026-10-29T14:26:38.310Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateCustomersStep]
2026-10-29T14:26:38.351Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.CustomerGenerationTasklet  : Starting customer generation...
2026-10-29T14:26:39.358Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.CustomerGenerationTasklet  : 100000 customers generated
2026-10-29T14:26:39.398Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateCustomersStep] executed in 1s84ms
2026-10-29T14:26:39.402Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateAccountsStep]
2026-10-29T14:26:39.414Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.AccountGenerationTasklet   : Starting account generation...
2026-10-29T14:26:39.418Z  INFO 1 --- [batchstarter] [           main] c.q.b.s.BankAccountGeneratorService      : Generating empty IBAN for accountId=10
2026-10-29T14:26:42.163Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.AccountGenerationTasklet   : 165115 accounts generated
2026-10-29T14:26:42.167Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateAccountsStep] executed in 2s765ms
2026-10-29T14:26:42.170Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateTransactionsStep]
2026-10-29T14:26:42.172Z  INFO 1 --- [batchstarter] [           main] c.q.b.t.TransactionGenerationTasklet     : Starting transaction generation...
2026-10-29T14:26:52.875Z  INFO 1 --- [batchstarter] [           main] c.q.b.t.TransactionGenerationTasklet     : 1000000 transactions generated
2026-10-29T14:26:52.878Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateTransactionsStep] executed in 10s708ms
2026-10-29T14:26:52.882Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [customerImportStep]
2026-10-29T14:30:17.004Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [customerImportStep] executed in 3m24s122ms
2026-10-29T14:30:17.006Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [accountImportStep]
2026-10-29T14:30:17.022Z  INFO 1 --- [batchstarter] [         task-3] c.q.b.b.i.a.AccountProcessorConfig       : ERREUR TEMPORAIRE SIMULEE for item with id=1
2026-10-29T14:30:17.165Z  INFO 1 --- [batchstarter] [         task-7] c.q.b.b.i.a.AccountProcessorConfig       : IBAN manquant for item with id=10
2026-10-29T14:36:28.297Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [accountImportStep] executed in 6m11s290ms
2026-10-29T14:36:28.301Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [transactionImportStep]
2026-10-29T14:30:17.004Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [customerImportStep] executed in 36m04s852ms

PARALLEL :
2026-10-29T15:01:25.900Z  INFO 1 --- [batchstarter] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2026-10-29T15:01:27.430Z  INFO 1 --- [batchstarter] [           main] o.s.d.j.r.query.QueryEnhancerFactories   : Hibernate is in classpath; If applicable, HQL parser will be used.
2026-10-29T15:01:40.322Z  INFO 1 --- [batchstarter] [           main] c.q.b.BatchstarterApplication            : Started BatchstarterApplication in 72.431 seconds (process running for 77.527)
2026-10-29T15:01:40.382Z  INFO 1 --- [batchstarter] [           main] o.s.b.b.a.JobLauncherApplicationRunner   : Running default command line with: []
2026-10-29T15:01:40.895Z  INFO 1 --- [batchstarter] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=dataWorkflowJob]] launched with the following parameters: [{}]
2026-10-29T15:01:41.022Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateCustomersStep]
2026-10-29T15:01:41.536Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.CustomerGenerationTasklet  : Starting customer generation...
2026-10-29T15:01:44.876Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.CustomerGenerationTasklet  : 100000 customers generated
2026-10-29T15:01:44.907Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateCustomersStep] executed in 3s874ms
2026-10-29T15:01:44.909Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateAccountsStep]
2026-10-29T15:01:44.910Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.AccountGenerationTasklet   : Starting account generation...
2026-10-29T15:01:44.925Z  INFO 1 --- [batchstarter] [           main] c.q.b.s.BankAccountGeneratorService      : Generating empty IBAN for accountId=10
2026-10-29T15:01:53.499Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.AccountGenerationTasklet   : 164820 accounts generated
2026-10-29T15:01:53.502Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateAccountsStep] executed in 8s592ms
2026-10-29T15:01:53.515Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateTransactionsStep]
2026-10-29T15:01:53.516Z  INFO 1 --- [batchstarter] [           main] c.q.b.t.TransactionGenerationTasklet     : Starting transaction generation...
2026-10-29T15:02:20.073Z  INFO 1 --- [batchstarter] [           main] c.q.b.t.TransactionGenerationTasklet     : 1000000 transactions generated
2026-10-29T15:02:20.075Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateTransactionsStep] executed in 26s560ms
2026-10-29T15:02:20.076Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [customerImportStep]
2026-10-29T15:04:24.227Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [customerImportStep] executed in 2m4s150ms
2026-10-29T15:04:24.228Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [accountImportStep]
2026-10-29T15:04:24.237Z  INFO 1 --- [batchstarter] [        batch-1] c.q.b.b.i.a.AccountProcessorConfig       : ERREUR TEMPORAIRE SIMULEE for item with id=2
2026-10-29T15:04:24.303Z  INFO 1 --- [batchstarter] [        batch-3] c.q.b.b.i.a.AccountProcessorConfig       : IBAN manquant for item with id=10
2026-10-29T15:07:20.824Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [accountImportStep] executed in 2m56s595ms
2026-10-29T15:07:20.826Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [transactionImportStep]
2026-10-29T15:20:58.506Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [transactionImportStep] executed in 13m37s680ms
2026-10-29T15:20:58.514Z  INFO 1 --- [batchstarter] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=dataWorkflowJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 19m17s502ms

PARTITION (sur transactions uniquement) :
2026-10-30T14:52:58.331Z  INFO 1 --- [batchstarter] [           main] org.hibernate.orm.core                   : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2026-10-30T14:52:58.341Z  INFO 1 --- [batchstarter] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2026-10-30T14:52:58.505Z  INFO 1 --- [batchstarter] [           main] o.s.d.j.r.query.QueryEnhancerFactories   : Hibernate is in classpath; If applicable, HQL parser will be used.
2026-10-30T14:52:59.081Z  WARN 1 --- [batchstarter] [           main] o.s.b.c.l.AbstractListenerFactoryBean    : org.springframework.batch.infrastructure.item.ItemStreamReader is an interface. The implementing class will not be queried for annotation based listener configurations. If using @StepScope on a @Bean method, be sure to return the implementing class so listener annotations can be used.
2026-10-30T14:52:59.732Z  INFO 1 --- [batchstarter] [           main] c.q.b.BatchstarterApplication            : Started BatchstarterApplication in 12.564 seconds (process running for 14.541)
2026-10-30T14:52:59.739Z  INFO 1 --- [batchstarter] [           main] o.s.b.b.a.JobLauncherApplicationRunner   : Running default command line with: []
2026-10-30T14:52:59.887Z  INFO 1 --- [batchstarter] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=dataWorkflowJob]] launched with the following parameters: [{}]
2026-10-30T14:52:59.918Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateCustomersStep]
2026-10-30T14:52:59.939Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.CustomerGenerationTasklet  : Starting customer generation...
2026-10-30T14:53:00.518Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.CustomerGenerationTasklet  : 100000 customers generated
2026-10-30T14:53:00.524Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateCustomersStep] executed in 606ms
2026-10-30T14:53:00.526Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateAccountsStep]
2026-10-30T14:53:00.527Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.AccountGenerationTasklet   : Starting account generation...
2026-10-30T14:53:00.530Z  INFO 1 --- [batchstarter] [           main] c.q.b.s.BankAccountGeneratorService      : Generating empty IBAN for accountId=10
2026-10-30T14:53:01.637Z  INFO 1 --- [batchstarter] [           main] c.q.b.tasklet.AccountGenerationTasklet   : 165207 accounts generated
2026-10-30T14:53:01.638Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateAccountsStep] executed in 1s112ms
2026-10-30T14:53:01.639Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [generateTransactionsStep]
2026-10-30T14:53:01.642Z  INFO 1 --- [batchstarter] [           main] c.q.b.t.TransactionGenerationTasklet     : Starting transaction generation...
2026-10-30T14:53:07.236Z  INFO 1 --- [batchstarter] [           main] c.q.b.t.TransactionGenerationTasklet     : 1000000 transactions generated
2026-10-30T14:53:07.237Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [generateTransactionsStep] executed in 5s598ms
2026-10-30T14:53:07.239Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [customerImportStep]
2026-10-30T14:54:52.845Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [customerImportStep] executed in 1m45s606ms
2026-10-30T14:54:52.847Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [accountImportStep]
2026-10-30T14:54:52.856Z  INFO 1 --- [batchstarter] [        batch-2] c.q.b.b.i.a.AccountProcessorConfig       : ERREUR TEMPORAIRE SIMULEE for item with id=2
2026-10-30T14:54:52.910Z  INFO 1 --- [batchstarter] [        batch-1] c.q.b.b.i.a.AccountProcessorConfig       : IBAN manquant for item with id=10
2026-10-30T14:59:07.519Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [accountImportStep] executed in 4m14s671ms
2026-10-30T14:59:07.521Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Executing step: [transactionImportStep]
2026-10-30T14:59:07.526Z  INFO 1 --- [batchstarter] [           main] c.q.b.partition.TransactionPartitioner   : Partition 0 => startLine=1, endLine=250000
2026-10-30T14:59:07.527Z  INFO 1 --- [batchstarter] [           main] c.q.b.partition.TransactionPartitioner   : Partition 1 => startLine=250001, endLine=500000
2026-10-30T14:59:07.527Z  INFO 1 --- [batchstarter] [           main] c.q.b.partition.TransactionPartitioner   : Partition 2 => startLine=500001, endLine=750000
2026-10-30T14:59:07.527Z  INFO 1 --- [batchstarter] [           main] c.q.b.partition.TransactionPartitioner   : Partition 3 => startLine=750001, endLine=1000000
2026-10-30T14:59:07.531Z  INFO 1 --- [batchstarter] [        batch-7] o.s.batch.core.step.AbstractStep         : Executing step: [transactionWorkerStep:partition3]
2026-10-30T14:59:07.531Z  INFO 1 --- [batchstarter] [        batch-5] o.s.batch.core.step.AbstractStep         : Executing step: [transactionWorkerStep:partition1]
2026-10-30T14:59:07.531Z  INFO 1 --- [batchstarter] [        batch-3] o.s.batch.core.step.AbstractStep         : Executing step: [transactionWorkerStep:partition0]
2026-10-30T14:59:07.531Z  INFO 1 --- [batchstarter] [        batch-2] o.s.batch.core.step.AbstractStep         : Executing step: [transactionWorkerStep:partition2]
2026-10-30T14:59:07.591Z  INFO 1 --- [batchstarter] [        batch-7] c.q.b.b.i.t.TransactionReaderConfig      : Reader initialized startLine=750001, endLine=1000000
2026-10-30T14:59:07.600Z  INFO 1 --- [batchstarter] [        batch-5] c.q.b.b.i.t.TransactionReaderConfig      : Reader initialized startLine=250001, endLine=500000
2026-10-30T14:59:07.622Z  INFO 1 --- [batchstarter] [        batch-2] c.q.b.b.i.t.TransactionReaderConfig      : Reader initialized startLine=500001, endLine=750000
2026-10-30T14:59:07.630Z  INFO 1 --- [batchstarter] [        batch-3] c.q.b.b.i.t.TransactionReaderConfig      : Reader initialized startLine=1, endLine=250000
2026-10-30T15:15:43.884Z  INFO 1 --- [batchstarter] [        batch-3] o.s.batch.core.step.AbstractStep         : Step: [transactionWorkerStep:partition0] executed in 16m36s352ms
2026-10-30T15:15:45.154Z  INFO 1 --- [batchstarter] [        batch-7] o.s.batch.core.step.AbstractStep         : Step: [transactionWorkerStep:partition3] executed in 16m37s623ms
2026-10-30T15:15:47.952Z  INFO 1 --- [batchstarter] [        batch-5] o.s.batch.core.step.AbstractStep         : Step: [transactionWorkerStep:partition1] executed in 16m40s421ms
2026-10-30T15:15:50.806Z  INFO 1 --- [batchstarter] [        batch-2] o.s.batch.core.step.AbstractStep         : Step: [transactionWorkerStep:partition2] executed in 16m43s275ms
2026-10-30T15:15:50.830Z  INFO 1 --- [batchstarter] [           main] o.s.batch.core.step.AbstractStep         : Step: [transactionImportStep] executed in 16m43s308ms
2026-10-30T15:15:50.844Z  INFO 1 --- [batchstarter] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=dataWorkflowJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 22m50s920ms

Copilot said:

Les résultats sont franchement intéressants.

CustomerImportStep
Mode	Temps
Default	3m24
Parallel	2m04

Gain :
(204 - 124) / 204 ≈ 39 %
➡️ Très bon gain.

AccountImportStep
Mode	Temps
Default	6m11
Parallel	2m56

Gain :
(371 - 176) / 371 ≈ 52 %
➡️ Excellent gain.

TransactionImportStep
Mode	Temps
Default	~36m05
Parallel	13m38

Gain :
(2165 - 818) / 2165 ≈ 62 %
➡️ Là c'est spectaculaire.

Batch global
Temps total :

Default
Customer : 3m24
Account : 6m11
Transaction : 36m05
Total ≈ 45m40

Parallel
Plain Text
Customer : 2m04
Account : 2m56
Transaction : 13m38
Total ≈ 19m18

Gain global :
45m40 -> 19m18
≈ -58 %
La conclusion qui m'intéresse
Je pensais que le reader synchronisé allait devenir un goulot d'étranglement.
Ce n'est manifestement pas le cas.

Le gain sur :
TransactionImportStep
est énorme :

36 min -> 13 min

Donc ton coût est probablement :

JPA/Hibernate
Mapping
Oracle
Writer

et pas la lecture du CSV.

Ce que je ferais pour la démo

Je ne toucherais plus à :

Plain Text
customerImportStep
accountImportStep

Je laisserais :

Plain Text
Customer -> Parallel
Account -> Parallel

Les gains sont déjà excellents.
Là où le partitioning devient intéressant
Tu as maintenant un benchmark de référence :

Plain Text
Transaction
36 min (séquentiel)
13 min (parallel)

Le partitioning devra faire mieux que :

Plain Text
13 min

Sinon il ne sert à rien. Et ça, c'est un vrai critère de décision.

Mon intuition

Avec :

1 000 000 transactions


et un index sur : BANK_ACCOUNT(IBAN)


je m'attends à quelque chose comme :

Transaction partitionné :
8 à 10 minutes
Pas forcément beaucoup moins.

Le gain énorme a déjà été obtenu par le TaskExecutor.
Pour la démo Sprint

Je montrerais même :

Séquentiel : 45 min
Parallel : 19 min
Gain : 58 %

puis :

Travaux suivants :
- Partitioning sur les transactions
- Comparaison avec TaskExecutor

Parce qu'avec ces chiffres, tu as déjà une démonstration très convaincante de la valeur du parallélisme Spring Batch

Le partitioning n'aide pas à réduire le temps de traitement global, 
parce que chaque partition parcourt un certain nombre de lignes qu'il doit skip.
Mais il permet de mieux répartir la charge et d'optimiser l'utilisation des ressources.

Ce qu'il faudrait faire pour avoir un gain supplémentaire :
1. Génération du fichier
2. Découpage en 4 fichiers
3. MultiResourcePartitioner
4. 4 workers

Sprint 7
✅ Enrichissement (batch)
✅ Détection de fraude (batch)
✅ Reporting (batch)
✅ Prometheus
✅ Grafana
✅ Endpoint /api/batch/status

Sprint 8 :
✅ Finaliser les TU
✅ Intégration Sonar
✅ Endpoint /api/batch/status
✅ Loki, Prometheus, Opentelemetry (profils), Grafana
✅ README professionnel

TODO :
- Reprendre sprint 7
