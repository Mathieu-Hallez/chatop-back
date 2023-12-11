![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

# ChâTop : Connection portal

We are creating a portal to connect future tenants and owners for seasonal rentals on the Basque coast initially and,
later, throughout France.

---

## Index

- [Quick Start](#Quick Start)
- [MySQL Database](#MySQL Database)
- [Generate a Private Key (RSA)](#Generate-a-Private-Key-(RSA):)
- [Project Structure](#Project structure)

---
## Quick Start

To launch the project, you have need of MySQL installed and [configured](#MySQL Database) on your computer.
Start MySQL service on your computer. If you need it you can configure MySQL access of the project in application.properties.

### Build Spring Boot Project with Maven
To be able to run your Spring Boot app, you will need to first build it. To build and package a Spring Boot app into a single executable Jar file with a [Maven](https://maven.apache.org/), use the below command. You will need to run it from the project folder containing the pom.xml file.
```shell
maven package
```
or you can also use
```shell
mvn install
```

### Run Spring boot app with java -jar command
To run your Spring Boot app from a command line in a Terminal window, you can you the java -jar command. This is provided your Spring Boot app was packaged as an executable jar file.
```shell
java -jar target/mywebserviceapp-0.0.1-SNAPSHOT.jar
```

### Run Spring boot app using Maven
You can also use the Maven plugin to run your Spring Boot app. Use the below example to run your Spring Boot app with the Maven plugin:
```shell
mvn spring-boot:run
```

---

## MySQL Database

First install [MySQL](https://www.mysql.com/fr/) on your environment

### 1. Create the user
In a MySQL terminal create a user `chatop` with a password `chatop`.
> You can create your own user with a different username and password. Just don't forget to change selected user in the `application.properties`.
```mysql
CREATE USER 'chatop'@'localhost' IDENTIFIED BY 'chatop';
```
Also grant some privileges to your new MySQL user.
```mysql
GRANT SELECT, INSERT, UPDATE, DELETE ON rentaldb.* TO 'chatop'@'localhost';
```

### 2. Create the database
Still in a MySQL terminal create a new database named: `rentaldb` and use it.
````mysql
CREATE DATABASE rentaldb;
````
````mysql
USE rentaldb;
````
Once the database created generate them by executing `generate_database.sql` script that you can find in `./src/main/resources/sql` :
```mysql
source ./generate_database.sql;
```

---

## Generate a Private Key (RSA):

In the `src/main/resources/certs` directory open a terminal and execute:
````shell
openssl genpkey -algorithm RSA -out private-key.pem
````
This command generates an RSA private key and saves it to the `private-key.pem` file.
Extract the public key from the private key by running:
````shell
openssl rsa -pubout -in private-key.pem -out public-key.pem
````

If needed, you can convert it to the appropriate PCKS format and replace the old one.
````shell
openssl pkcs8 -topk8 -inform PEM -outform PEM -in private-key.pem -out private-key.pem -nocrypt
````
---

## Project structure

| Folder     | Utility                           |
|:-----------|:----------------------------------|
| Controller | API controllers.                  |
| Model      | Model to communicate with the DB. |
| Service    | Processing of received data.      |
| Repository | Database request access.          |
| Payload    | Data to object (DTO).             |

## Contribute to the project
ChâTop is an open source project.

## Authors
Our code squad : 
- Mathieu HALLEZ

## Licensing

This project was built under the MIT licence.
