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
### Step 2 - Populate cofiguration file
### Step 3 - Run grep client