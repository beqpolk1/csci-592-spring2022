[(home)](https://beqpolk1.github.io/csci-592-spring2022/)

## Test Plan

### Goals

**Part 1:**

* Answer three types of question:
  1. When the same data is present in multiple databases, which one should I access it from?
  2. When data for the same U-Schema entity is spread out between multiple databases, how do I tell where to search to satisfy a query for that entity?
  3. How do I join different data from different databases together?
* Which will require:
  * Having data for the same entity represented in at least two different databases (1), which will require transforming one schema into another
  * Having data for the same *entity type* but for different *specific entities* in at least two different databases (2)
  * Having data that is for different entity types and semantically related in at least two different databases (3)
* These questions will be subject to **correctness tests**: does query processing return a correct result
  * Could be determined through item counts
* Goal:
  * Have data stored in 3 different database systems: graph, relational, and document
  * Define and process at least one specific query for each question above and test for correctness; two queries per question would be better

**Part 2:**

* Evaluating queries in an efficient manner
  * What type of queries?
    * Similar to queries considered for the above questions
    * The U-Schema paper focuses mostly on "select" style statements, so analyzing OLAP style data use
  * This will require a cost measurement: previous literature would support time, cardinality, secondary storage accesses
    * Time (wall clock): not generalizeable, but would allow separating cost of "native queries" from cost of processing at the meta-level
    * Cardinality: could be a good measurement, as processing fewer data items will generally lead to better performance
    * Secondary storage accesses: very difficult to measure since the system being constructed is not the one reading from disk, individual DB's do that step
    * System memory: not much of a history of this, but since this system will involve handling large amounts of data in memory it could be relevant
* Which will require:
  * Defining a cost estimation mechanism
  * Defining at least two different execution plans for the same query such that the same result is obtained, but intermediary steps are different
    * Will need to define different processing mechanisms such that different execution plans can be generated (e.g. nested loop join vs. block join)
    * Could define an indexing data structure, and have one plan disregard it while another plan uses it
  * Estimating the costs for each execution plan
  * Observing true cost for each execution plan and verifying that each returns the correct results
* These questions will be subject to **quantitative tests**
  * Do the cost estimates align with true costs
  * Did the supposed optimization methods yield lower true costs
* Goal:
  * Have at least two different queries to analyze; these could overlap with those used for part 1; more queries would be better
  * Have at least two different execution plans for each query; more execution plans could be useful
  * Have at least two different size datasets to test against

## Research

### Query optimization by semantic reasoning:
https://www.proquest.com/docview/303207778?pq-origsite=gscholar&fromopenview=true
* Structure example database "to illustrate the special capabilities..." of the project
* Specifically covers scenarios to be handled by the project
* Quantitative estimates of improvements involve set of assumptions on how operations are performed
  * Form cost estimate equations based on assumptions
  * Introduction of techniques results in different equations with lower results
* Also run quantitative experiments
  * Specify assumed values of database properties (e.g. # records per relation)
  * Queries are chosen to illustrate transformations/capabilities of the project
  * List specific queries, analyze them, apply transformations, estimate costs according to equations for each version
  
### Analyzing Plan Diagrams of Database Query Optimizers
https://dsl.cds.iisc.ac.in/publications/conference/picasso-revised.pdf
* Uses TPC-H decision support benchmark: https://www.tpc.org/tpch/default5.asp
  * Slightly modifies some queries better target issues in paper
* TPC-H is a fixed database schema and queries used for benchmarking
  * Designed to simulate some kind of large scale commerce application
  * Relational only
* There is also a TPC benchmarking tool for Data Integration (warehousing)

### LEO - DB2's LEarning Optimizer
https://www.vldb.org/conf/2001/P019.pdf
* Also uses TPC-H benchmarking

### Consistently Estimating the Selectivity of Conjuncts of Predicates
https://courses.cs.washington.edu/courses/cse544/11wi/papers/markl-vldb-2005.pdf
* Used real-world 1GB relational database and 200 queries from a DMV workload
* Data fit with aspects project seeked to improve (one relation had strong correlations to use for multivariate statistics)
* Established base cardinalities, then examined estimated cardinalities for sample queries
* Implemented project techniques, examined estimated cardinalities for sample queries
* Compared against true cardinalities of sample queries

### Performance Benchmark PostgreSQL / MongoDB
https://info.enterprisedb.com/rs/069-ALB-339/images/PostgreSQL_MongoDB_Benchmark-WhitepaperFinal.pdf
* Seemed promising at first - comparing performance of Postgres and Mongo on *same datasets*
* Distinguished between transactional, OLTP, and OLAP workloads
* For OLAP (probably closest style to goals for my project), the dataset was just JSON loaded into Postgres (not native relational representation)
  * Authors did not have same data represented two different ways
* Leads to questions of what kind of workload I'm attempting to explore - probably OLAP-esque
  * U-Schema paper focuses on data reads for their generic query engine
  
### Evaluation of Database Access Paths
https://dl.acm.org/doi/abs/10.1145/509252.509273?casa_token=sNKKNe0G9OcAAAAA:mR6muOYDOvzZX-AEgXI6w3x7L3C4TxZ1FTWOJTK8T9FsPIQVaTd7FPWPilv259DP0okIwqwlV5yH
* Focuses on "access paths" - "a series of steps which must be taken in order to search the database and retrieve the data requested by the user"
* Paper seeks to "develop a more general cost model for access methods"
* Choose cost unit to be secondary storage accesses required
* Compare their cost models, using novel access path techniques, to models put forth by previous authors
* Show that their cost model matches some cases from other authors, and that then their improvements actually lessen estimated cost
* Don't appear to evaluate "real" costs at all?

### Query Optimization in Database Systems
https://dl.acm.org/doi/abs/10.1145/356924.356928
* Very good review that covers a lot of query optimization research
* Some of above articles taken from references of this paper
* Some other relevant-sounding articles not available with MSU Library access

### Uniform Access to Non-relational Database Systems: The SOS Platform
https://link-springer-com.proxybz.lib.montana.edu/chapter/10.1007%2F978-3-642-31095-9_11
* Came up with an example application that would use multiple databases to exercise their platform
  * "...adopt the perspective of a Web 2.0 development team that wants to benefit from the use of different NoSQL systems."
  * Authors mock up a simplified version of Twitter for example purposes
* Don't use a predefined dataset or perform any experiments, but use example app/schema to demonstrate platform functionality

### Uniform data access platform for SQL and NoSQL database systems
https://www-sciencedirect-com.proxybz.lib.montana.edu/science/article/pii/S0306437916303398
* Authors demonstrate their platform with a test dataset and experiments
  * However, no references are made to **where** this test data came from or how it was loaded/structured into relational vs. NoSQL stores
  * Authors describe the schema of the test data and how it suits demonstrating their platform (webshop database)
* Run tests with predefined queries on datasets of varying sizes
  * All datasets are relatively large (100,000 to 1.4 million entities)
  * Queries gradually add more selected columns which use more objects, and some have filter conditions
* Relevant point may be how they measure performance
  * Use time metrics based on measurements from the test machine
  * Differentiate between:
    * Entire running time: "Time measurement began when the user sent the request to [the system], and the measurement ended when the queried data was available in [the system] in JSON format."
	* Data retrieval time: "...began when the user request was sent from [the system] to the data source and ended when the queried data was available in [the system] in JSON format."
	* Native query time: "...the running time of native database queries."
  * For my tests, it will be relevant to track and analyze native query times separately from data retrieval times
  
### A unified metamodel for NoSQL and relational databases
https://www-sciencedirect-com.proxybz.lib.montana.edu/science/article/pii/S0306437921001149
* Testing is oriented around a few specific goals:
  1. Make sure a correct U-Schema is inferred from data
  2. Make sure that all entity type variations in the extracted U-Schema are represented in the database (U-Schema didn't "make something up")
  3. Make sure that the the number of the objects in the database matches the sum of objects that belong to each entity type variation in the extracted U-Schema (there are no "unaccounted" objects in the database)
  4. Testing scalability of schema-inference process
* Authors generated synthetic test data:
  * Defined a sample U-Schema with desired structure (User profile with watched movies)
  * Randomly created elements in the target databse according to the defined model
  * Generated four test datasets of various sizes
  * Useful for testing points 1-4 above
  * A closer reading indicates that the authors **did not** use the synthetic dataset for testing on the relational model
  * The tool does still seem to be available, but only for generating synthetic data for NoSQL databases
* For each data paradigm, authors also used a non-synthetic dataset
  * MongoDB, Redis, and HBase - the [every politician dataset](https://everypolitician.org/)
  * Neo4j - the Movies dataset available on the Neo4j website (now normally only "built in", but the [author's repository](https://github.com/catedrasaes-umu/NoSQLDataEngineering/) has a copy)
  * MySQL - the [Sakila database](https://dev.mysql.com/doc/index-other.html)
  * These were useful for testing points 2-4 above
* These datasets may prove to be useful testing material; the types of tests and methods used by the authors are likely not applicable to my project, however