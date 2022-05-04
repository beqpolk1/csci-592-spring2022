[(home)](https://beqpolk1.github.io/csci-592-spring2022/)

## Using Docker for the Project

* To increase portability and provide a system-independent test environment, I decided to employ Docker containers
* There are separate containers to host each database and the different applications that are part of the project
* I will be developing using the IntelliJ IDE from JetBrains, which was also configured to run Java applications in a Docker container
	* In addition, I can use a debug configuration to allow for remote debugging of apps running in a Docker container

#### Note: development was done on a Windows 10 machine and a WSL Ubuntu distribution, using Docker Desktop (with the WSL 2 backend)

* Configuring everything and getting the containers talking was a bit of a crash course in Docker networking
* The most important breakthrough was to use the **value host.docker.internal** when networking containers together
	* This seems to be a value only available for Docker desktop (and possibly Docker for Mac) that points to the host Docker address
	* Any time I needed an application in one container to talk with another (e.g. using the Mongo or Postgres shells), I would direct it to this address and then use the relevant portability
	* In the "config.yml" file for ModelUM's data generation application, I *had to* use an actual IP address for the target database (because of their validation rules)
	* This IP was found with a simple "ping host.docker.internal"  command, and is machine-specific
	
### Description of Docker Files and Infrastructure

* All files related to Docker images and running containers are in the "project/docker" directory
* Images (Dockerfiles) are located in each subdirectory
    * basic_env: image with Java 8, used to run data generator; built as "csci-592/env"
    * clients: image with clients for interacting with databases (e.g. Mongo shell, Psql terminal); built as "csci-592/clients"
    * mongo: image with MongoDB; built as "adv-db/mongo"
    * neo4j: image with Neo4j; built as "adv-db/neo4j"
    * postgres: image with PostgreSQL; built as "adv-db/postgres"
* Shell scripts to run each container are located in the root of the "docker" directory
    * data_gen.sh: starts a csci-592/env container and runs the NoSQL data generation application from the [NoSQL Data Engineering repo](https://github.com/catedrasaes-umu/NoSQLDataEngineering)
        * Note that this mounts a volume pointing to the directory where the config.yaml file for data generation can be found
    * mongo_client.sh: starts a csci-592/clients container and launches the Mongo shell to connect to the mongoSongs database
    * mongo_run.sh: starts a adv-db/mongo container to host MongoDB
    * postgres_client.sh: starts a csci-592/clients container and launches the Psql terminal to connect to PostgreSQL
    * postgres_run.sh: starts a adv-db/postgres container to host PostgreSQL