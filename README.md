### Group Finance service

#### Requirements:
- Maven
- Docker
- Python3 (for client)

#### How to use:
1. In project's root folder run `mvn clean package` or build it in IDE
2. `sudo docker network create group-finance-network` to create a network in Docker
3. `sudo docker run -d --name mongo-container --network=group-finance-network -v ~/mongo-data:/data/db mongo`
to create a MongoDB container
4. `sudo docker build -t group-finance .` to create an image of our service
5. `sudo docker run -p 8080:8080 --name group-finance-container --network=group-finance-network group-finance`
6. Use the service. The service is accessible on http://localhost:8080 or simply use the client.

#### Client:
If you want to use the command line client, check out README in /client folder.

#### Simulation:
Project contains 3 end-to-end tests that simulate real world usage.
One of the tests simulate usage as given in Mediatoolkit's task.

NOTE: End-to-end tests run on a local MongoDB instance. They create a new collection: "group-finance-integtest"
 and clean up the collection when they're finished.