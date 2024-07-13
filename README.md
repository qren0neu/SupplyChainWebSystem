# SupplyChainWebSystem

A web system that handles the management problem of the supply chain.

Architecture:

![image](https://github.com/user-attachments/assets/96afcebf-52d2-41ef-957d-cfd892d8bb0b)


Log in as a supplier, manufacturer, distributor, or end customer.

The system will help you to determine the best time/price options.

What is included: 
- Spring Boot backend systems as a portal, manufacturer, distributor or end customer
- Spring Cloud Eureka microservices management
- Redis for user info caching

## How to run?

### Dependencies

Download eclipse: https://www.eclipse.org/downloads/packages/

Install Java JDK 17 on your machine.

Or try with the Docker Spring environment: https://spring.io/guides/gs/spring-boot-docker

Install Redis.

Import project and load Maven dependencies.

Run SQL files in `/sql` folder in Postgres/MySQL.

### Running

Go to the `Application` file of each folder.

Do `run as Java file`.

Starting sequence:
- Database
- SupplyChainServer
- Supplier
- Manufacturer
- Distributor
- Portal

Then go to http://localhost:8081
