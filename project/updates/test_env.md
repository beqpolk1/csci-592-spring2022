[(home)](https://beqpolk1.github.io/csci-592-spring2022/)

## DataConverter and Initializing the Test Bed

* This document covers a little bit about the DataConverter program/project, and covers the steps necessary to get the test bed up and running

### DataConverter

* In the "project" directory of the repository is a directory for the DataConverter project, which is an IntelliJ IDE project folder
* This is the program that connects to a MongoDB source database, populated with entities according to the MongoSongs schema, and ports those entities to relational (Postgres) and graph (neo4j) formats
* Quick dependency rundown:
    * JDK 8
    * MongoDB driver: org.mongodb:mongo-java-driver:3.5.0 (https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver/3.5.0)
    * PostgreSQL driver: postgresql-42.3.5 (https://jdbc.postgresql.org/download.html) (downloaded as a jar and included in project directory)
    * Neo4j driver: org.neo4j.driver:neo4j-java-driver:4.4.4 (https://mvnrepository.com/artifact/org.neo4j.driver/neo4j-java-driver/4.4.4)
    * Other libraries as required by the above dependencies (e.g. reactive-streams-1.0.3 required by Neo4j driver)
* Project is configured to build a standalone, artifact jar that includes all dependencies

### Running the Test Bed

> * All the relevant pieces for the testing environment are set up to run in Docker containers
> * The repository includes bash scripts, each of which handles launching one of the pieces of the test environment
> * See [the page on Docker](using_docker.md) for more details

> Note that there are hardcoded directory references in some of the Docker bash scripts; running these on a different machine may require adjusting these references

* Begin by starting the Mongo database using the ```mongo_run.sh``` file
* Now that Mongo is running, you should be able to generate the test data by running the ```data_gen.sh``` file
* Once the data is generated, you may wish to verify that it's there; you can do so by starting the Mongo client using the ```mongo_client.sh``` file and issuing some queries
* The next step is to start the target databases, Postgres and Neo4j
    * Launch Postgres using the ```postgres_run.sh``` file
        * From here, it will be necessary to first create the target database and tables for the converter
        * Launch the Postgres client using the ```postgres_client.sh``` file
        * Create the database by issuing the command ```\i /mnt/postgresScripts/create_database.sql```
        * Connect to the database by issuing the command ```\c mongosongs```
        * Build the tables by issuing the command ```\i /mnt/postgresScripts/create_tables.sql```
    * Launch Neo4j using the ```neo4j_run.sh``` file
* Now we are ready to convert the data from Mongo to the target databases! Run the DataConverter using the ```data_convert.sh``` file
* At this point, you should be able to explore any of two target databases with some queries and verify the data is there; some sample Neo4j queries are included in the file "useful_neo4j_queries.txt"
    * Use the Postgres client to explore Postgres
    * For Neo4j, it's recommended to access the browser-based user interface
* Note that to re-initialize the test bed, it will be necessary to either spin up new Docker containers or delete data from the databases you already have running
    * If you want to re-run the DataConverter, delete all data from both the Postgres and Neo4j databases first
        * There is a query in the "useful_neo4j_queries" file that will delete everything from the database
        * For Postgres, you can clear out the database by re-running the command ```\i /mnt/postgresScripts/create_tables.sql``` (this will drop and recreate all the tables)
    * If you want to regenerate the data, you will also need to delete all collections from the Mongo database
        * Sample command would be ```db.artist.drop()```
        * Modify the config.yaml file or the MongoSongs.xmi file
        * Then start the process over again