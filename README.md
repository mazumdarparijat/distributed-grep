# Distributed grep
This project implements distributed grep functionality which enables querying of distributed 
log files on multiple machines, from any one of those machines.

## Design
A simple Client-server architecture has been used. All the machines from which log files have to be queried 
must have the server (called grep-server) running. A client (called grep-client) takes grep queries from user
 and displays back matching lines. The client additionally requires a configuration file which stipulates the addresses of 
 the servers and local logfile path in the machines.
 
The processing paradigm is as follows. The client takes the grep query from user and spawns multiple threads 
(each talking to one server). Each thread relays the query to its server. In the server, the grep query is executed
locally and the matching lines are relayed back to the client thread. The client thread then prints this matching line.

Multithreaded client and multithreaded server not only ensure faster execution due to paralellization, but also add fault tolerance.
The fault tolerance guarantee of the system is that it fetches answers from all machines that have not failed.
 
## Package Dependencies
- Java 7
- Maven

## Instructions
### Step 1 - Run all servers
grep-server has to be run in all machines containing required log files. The following are the steps to run server in one machine :
1. ssh into the vm machine : Eg - ```ssh <NETID>@fa15-cs425-gNN-XX.cs.illinois.edu```
2. Type ```git clone https://gitlab-beta.engr.illinois.edu/cs425-agupta80-pmazmdr2/mp1-distributed-logging.git```
3. cd into the project root directory
4. run ```mvn -DskipTests package```. You should see build success.
5. Type ```cd ./scripts/ && ./run_server <PORT_NUMBER_XX>```

### Step 2 - Populate cofiguration file
The configuration file has 3 lines : serverAddress, serverPort and logFilePath. This file stores 
the server address (fa15-cs425-gNN-XX.cs.illinois.edu), server port (same as <PORT_NUMBER_XX> in step 1.5) and 
filepath of the log file for all the machines. Refer to  ```./src/main/resources/config.properties``` for 
format details.

### Step 3 (Optional) - Run unit tests
Once the grep-servers are all running and configuration file is properly set, validation can be done by 
running unittest. This can be done by running ```mvn test``` in project root directory.

### Step 4 - Run grep client
grep-client takes grep query and grep matching control options from user. The configuration file path 
(populated in step 2) and the grep query string are required arguments, other switches are optional. The help 
switch shows usage instructions.
1. ```cd ./scripts/```
2. ```run_client -help```