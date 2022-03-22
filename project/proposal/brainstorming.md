# Potential Project Ideas

* Extending an existing model to have new functionality or support more operations, perhaps application specific
* Create a model that can work with at least two types of database system not covered by existing research

* Develop a tool that can ingest data from multiple database paradigms, transform into a standardized format, feed to...
  * A web application?
  * A machine learning application?
* On a similar note: take "data lakes" and aggregate the data through universal modeling techniques, lift it into *some sort of space* to generate "point clouds" and then apply learning algorithms

* Take Bernstein et al.'s ("A Vision of Management of Complex Models") model structure/definition and implement through a graph DB
  * "Challenge 1: Develop a mechanism for representing models and for storing those representations"
* Extend the ideas presented by Bernstein et al. to include modern types of NoSQL data stores
* Work on challenge from Bernstein et al.'s paper to find "best matches" between models
  * Find like fields between models and some probability of those fields being the same
  * *Learn* the best match between model schemas

* Extend Atzeni et al.'s NoAM data model to graphs and column stores
* Prove isomorphisms between datastores using the NoAM data model
* Explore the mathematical representation and implications of the NoAM model and the design guidelines given by Atzeni et al.

* Extend Ruiz et al.'s NoSQL schema inference to graphs and possibly streaming paradigms?
* Apply data mining similarity detection techniques to aid with inferring NoSQL schemas
* Integrate a data transformation tool into an application, based on the NoSQL schema and techniques from Ruiz et al.

* Take the universal metamodel from Atzeni et al.'s paper (A Universal Metamodel and Its Dictionary) and apply it to NoSQL datastores
  * Document store, column store, graph
  * See if proposed meta-constructs can adequately capture these systems - is it necessary to extend with more meta-constructs?
  * Fill in dictionary metadata for sample application

* Explore how to store data in a "U-schema" state and provide efficient access or context-sensitive processing and usage
* Apply some learning techniques to improve on the forward/reverse mappings presented in the Candel "U-schema" paper

---

#### From the discussion of [Inferring Versioned Schemas from NoSQL Databases and Its Applications](../../article_writeups/2.2_ruiz_morales_molina_versioned_schemas.md)

* Give two collections in Mongo, of the same entity, do I end up with any versions that are the same?
* If you find versions that are the same across diff. databases or collections, you start to build confidence that they're talking about the same thing
  * Quantifiable in terms of how much version overlap there is?
  * Detect same objects in different places
* Could you start to give some tooling to let users pick up which things are linked together across databases or collections?

* Names are largely irrelevant for unstructured data...
* Is there a way to do a similarity measure between structures of JSON/BSON documents?
* In relational terms, you can represent schemas as vectors (?) and then compute vector distance between two schemas
  * How to extend or do this within JSON schema language *(what is the name of this?)*
* There might be a path that yields better similarity - distance between trees?
* Look at how different papers come up with metamodels 