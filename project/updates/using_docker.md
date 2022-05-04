## Using Docker for the Project

* To increase portability and provide a system-independent test environment, I decided to employ Docker containers
* There are separate containers to host each database and the different applications that are part of the project
* I will be developing using the IntelliJ IDE from JetBrains, which was also configured to run Java applications in a Docker container
	* In addition, I can use a debug configuration to allow for remote debugging of apps running in a Docker container

#### Note: development was done on a Windows 10 machine and a WSL Ubuntu distribution, using Docker Desktop (with the WSL 2 backend)

* Configuring everything and getting the containers talking was a bit of a crash course in Docker networking
* The most important breakthrough was to **use the value host.docker.internal when networking containers together**
	* This seems to be a value only available for Docker desktop (and possibly Docker for Mac) that points to the host Docker address
	* Any time I needed an application in one container to talk with another (e.g. using the Mongo or Postgres shells), I would direct it to this address and then use the relevant portability
	* In the "config.yml" file for ModelUM's data generation application, I *had to* use an actual IP address for the target database (because of their validation rules)
	* This IP was found with a simple "ping host.docker.internal"  command, and is machine-specific
	
### Description of Docker Files and Infrastructure

* All files related to Docker images and running containers are in the "project/docker" directory

	