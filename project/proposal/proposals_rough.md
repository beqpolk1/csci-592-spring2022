# Project Options

## Option 1 - Query Optimizer Over U-Schema

* ["Unified Metamodel"](../../article_writeups/3.2_candel_ruiz_molina_unified_metamodel.md) authors present a query architecture based on U-Schema.
* Query architecture involves compiling a SQL-like query into an "abstract query", which is fed to an evaluator that evaluates it against a specific database.
* In this way, authors seem to rely on idea of running queries against individual databases in isolation.
  * Presumably, the individual databases have already developed and tuned mechanisms for query optimization over their relevant models.
  * Contrast this with something like [OrientDB](https://www.tutorialspoint.com/orientdb/orientdb_overview.htm), which uses a novel data definition structure to provide query efficiencies.
* What role does this give a schema optimizer under U-schema? What does querying look like under this model?
  * Supply a "simple" query (S-F-W) - how does it know which underlying databases store the data to answer?
  * How to make decisions about which databases to query?
    * Detect based on syntax what sort of data model is being queried against
    * Data could still be spread out among multiple databases (e.g. in both document store and key-value store)
  * What if data is stored across multiple databases (e.g. combining user cart data in a document store with user address data in relational tables)?
    * *Side question - is there actually a scenario where this could be more efficient or desirable?*
	* **Microservice architectures**
  * What if some databases store the same information, but it's faster to get it out of one than the other (e.g. aggregated order object from document store vs. tableized order info from relational store)?
  * How to identify and limit query to only specific structural variations?
  
* Points to a need for some sort of *data dictionary* storing metadata on what's represented for different entities in different models.
  * Would this have an advantage over dictionaries already present (presumably) for underlying databases?
* Use dictionary to estimate cost for accessing data under certain models, given access patterns defined by query, and execute most efficient option.
  * For example, a query that involves traversing a large number of relationships may be more efficient under the graph model than the relational model.
  * Accessing data that is already aggregated in some aggregate-oriented model is likely to be more efficient than combining records from a relational model.
  * OLAP-style queries are likely more efficient under a relational model.
* How to incorporate indexing to speed up query processing?
  * Where do these indices live?
  * What types of indices would be useful (hashes, B-trees, inverted index)?
  * Can we apply indices to some U-Schema model that helps to answer the types of questions above in combination with the dictionary?

* Need to find some example datasets?
  * Examine existing "U-Schema" project repo for clues on data generation? Email paper authors? 

#### Potential resources

* https://www.proquest.com/docview/2515175023/B54187C3DA8C4A24PQ/13?accountid=28148
* https://www.proquest.com/docview/2397274270
* https://dzone.com/articles/approaches-to-query-optimization-in-nosql-1
* https://dzone.com/articles/a-deep-dive-into-couchbase-n1ql-query-optimization
* https://web.stanford.edu/class/cs345d-01/rl/chaudhuri98.pdf
* https://ravendb.net/docs/article-page/5.3/csharp/studio/database/indexes/indexes-overview#indexes-overview



## Option 2 - Infer DB Entity Correspondance Through U-Schema Structure

* ["Unified Metamodel"](../../article_writeups/3.2_candel_ruiz_molina_unified_metamodel.md) paper builds on ideas from ["Inferring Versioned Schemas"](../../article_writeups/2.2_ruiz_morales_molina_versioned_schemas.md) paper for inferring schemas.
* However, this method relies on proper naming and structuring of database entities.
* Likewise, instantiating some U-Schema entities in "unsupported" data models (such as relationship entities from a graph in a relational model) involves creating some "placeholder" entities with generic names, or making decisions on how to reverse map certain entities.
* Is there a way to predict a correspondance or mapping between database entities based on their structure?
  * This would be similar to finding isomorphisms between database entities of different data models.
  * Note that an isomorphism does not necessarily imply two entities are the same.
* Finding structural similarities between entities may provide guidance on other reverse mappings that could best serve a database.
* U-Schema metamodel is object-relational, and could be represented as a tree (and therefore as a graph).
* Outline for method of detecting similarity:
  * Translate database entities to U-Schema (forward mapping)
  * Represent U-Schema structure as a tree
  * Compute similarity (distance) measures between different U-Schema instances
    * Note that algorithms already exist for computing graph isomorphisms
* What would be the benefits of this?
  * Give users suggestions between what might be corresponding database entities to aid with mappings
  * Infer "best" way to reverse map entities from U-Schema to other data models
  * Generate sets of unified representations of disparate data to mine for insights
  
#### Existing research