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
* Some other relevant-sounding articles not available with MSU Library

### Should review test data setups for some of previously read papers