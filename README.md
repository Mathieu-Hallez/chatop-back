![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

# ChâTop : Connection portal

We are creating a portal to connect future tenants and owners for seasonal rentals on the Basque coast initially and,
later, throughout France.

## Technologies
- Spring Boot 3
- Spring Security
- MySQL
- Maven
---

## MySQL Database

First you need to install [MySQL](https://www.mysql.com/fr/) on your environment

### Create a user
In a MySQL terminal create a user `chatop` with a password `chatop`.
> You can create your own user with a different username and password. Just don't forget to change selected user in the `application.properties`.
```mysql
CREATE USER 'chatop'@'localhost' IDENTIFIED BY 'chatop';
```
Also grant some privileges to your new MySQL user.
```mysql
GRANT SELECT, INSERT, UPDATE, DELETE ON rentaldb.* TO 'chatop'@'localhost';
```

### Create a database
Still in a MySQL terminal create a new database named: `rentaldb` and use it.
````mysql
CREATE DATABASE rentaldb;
````
````mysql
USE rentaldb;
````
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

## Contribute to the project
ChâTop is an open source project.

## Authors
Our code squad : Mathieu HALLEZ

## Licensing

This project was built under the Creative Commons licence.
