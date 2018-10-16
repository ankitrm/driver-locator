# driver-locator-service
This service helps to store and locate the driver location at any given point in time

Author: Ankit Vij

Steps to use the zip file:
1. Simply unzip/untar the zip file provided
2. Install sbt command line (brew install sbt)
3. On terminal, in the home directory of unzipped folder, run `sbt run`. Logs will be generated in /data/logs/driver-locator-service folder in text format as well as json format to be easily integrated with Kibana
4. To run all tests with coverage, simply run `sbt clean scalastyle test:scalastyle models/test:scalastyle coverage test models/test`.
5. To generate coverage report, run `sbt coverageReport`. Check out the coverage report in `target/scala-2.12/scoverage-report/index.html`.
6. To generate deployable tar/zip, run `sbt dist`. Inside target/universal folder `driver-locator-service-1.0.0-0.zip` would be generated.
7. To publish models, run `sbt publish` or `sbt publishLocal`


***Deployment details on environment***:
1. Copy the above generated `driver-locator-service-1.0.0-0.zip` file (Step 6) and paste it in any VM/Instance. Unzip it
2. On terminal, inside the folder, go to driver-service/bin and run `sh driver-locator-service -Dhttp.port=80` (select appropriate port, do not give parameter to run on default port 9000) & and hit enter and the service would be up

***NOTE***: The first call to service might take some time (it establishes all necessary connections and instantiates class opbjects), so please validate the performance froms second call onwards

***Tech Stack***
Used Scala on top of Play framework (sbt based non blocking framework supporting both Java and Scala as underlying PL's) and Mongodb as backend database.
Golang is fully functional in nature and has popular frameworks such as Beego and would have been equally good to implement this assignment.

There are other options as well instead of Play framework (Spring boot) as it gives a nice MVC framework to build up Microservices quickly with non blocking IO operations and nice functional and asynchronous support, friendly wrappers around AKKA for job scheduling with a pool of different templates to get started quickly. Moreover it provides user the flexibility to dig deep into the abstraction layer and implement our own DSLs and routers. Since we needed to bootstrap a web app that primarily serves up RESTful web services, both Play and Spring boot solves the purpose.

There are multiple DB's supporting geo spatial query (Postgres, MySql, MongoDb), but it depends on our requirement. If we want to concentrate more on ACID properties of DB (easier to implement in RDBMS), we would go for Postgres/ MySql but the tradeoff would be to have no mechanism for sharding the database across a cluster of nodes. When the dataset grows and there is a need for horizontal scaling, sharding would have to be done manually which is quite complex and time consuming task as compare to sharding mongodb. As noted by postgres documentation, "PostgreSQL does not provide the system software required to identify a failure on the primary and notify the standby database server", so users would have to rely on third party party clustering frameworks to provide the necessary availability and uptime. In our case, if primary goes down, customers would not be able to locate drivers which would result in direct revenue loss. Mongodb seems to be more developer friendly due to its BSON format structure and seemed to be quicker for implementation (due to comfort in writing JSON code).

***Automated Deployment pipeline***:
We can integrate the build pipeline on top of github with travis and using S3 as datastore to handle all the deployables. 
1. Once a PR is merged with master, travis build is invoked which will read .travis.yml file from the repo and perform the necessary deployment actions. 
2. Once packaging command is complete (`sbt dist` in our case), the deployable packaged component is pushed to S3 with current release version
3. The instances can be configured with scripts to pull the service from S3, unpackage the service and run the shell executable script (provided with sbt dist command)

***Infrastructure details***:
https://aws.amazon.com/ec2/instance-types/#instance-details
Two EC2 micro instance clustered using load balancer to split the requests across can be used. The number of instances can be 
increased for horizontal scaling if the appropriate performace is not met
For better db performace, sharded mongodb with read secondary and write primary should be configured since geo spacial query on thousands of records would be quite heavy.

***SAMPLE REQUESTS***

PUT: http://localhost:9000/drivers/1/location

BODY: {
      	"latitude" : 89,
      	"longitude" : 178,
      	"accuracy" : 2
      }

GET: http://localhost:9000/drivers/1.23/11.23?limit=10&radius=500000000