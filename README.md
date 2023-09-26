# bidhaa

[//]: # ([![bidhaa app]&#40;https://github.com/BILLthebuilder/bidhaa/actions/workflows/aws.yml/badge.svg&#41;]&#40;https://github.com/BILLthebuilder/bidhaa/actions/workflows/aws.yml&#41;)
## About

This is an Inventory management system that also provides authentication services:

- Create users
- Login users
- Update users
- Delete users

This app is built using Java(Spring Boot)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

- Java 17 sdk

- Postman

### Installing

A step by step series of examples that tell you how to get a development environment running

- Clone the project repository


HTTPS: `git clone https://github.com/BILLthebuilder/bidhaa.git`

SSH: `git clone git@github.com:BILLthebuilder/bidhaa.git`

- Change the directory

`cd auth-api`

- To compile a local build

```bash
./mvnw clean compile package
```

- To run a regular development build

```bash
java -jar /your_clone_directory/bidhaa/target/0.0.1.jar
```

### Test the API Endpoints

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/5176138-5f9442bb-8256-4171-90d9-e03d8ec3457b?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D5176138-5f9442bb-8256-4171-90d9-e03d8ec3457b%26entityType%3Dcollection%26workspaceId%3Df99137e8-f0b4-4850-8b9e-2fa166538946)

#### Auth Endpoints

| Request | Endpoint               | Function                  |
|---------|------------------------|---------------------------|
| POST    | `/api/v1/users/signup` | Register a new user       |
| POST    | `/api/v1/users/login`  | Login a registered user   |
| GET     | `/api/v1/users`        | Get  all registered users |
| PUT     | `/api/v1/users/{Id}`   | Update a registered user  |
| DELETE  | `/api/v1/users/{Id}`   | Delete a registered user  |

[//]: # (#### Product endpoints)

[//]: # ()
[//]: # (| Request | Endpoint                | Function                  |)

[//]: # (|---------|-------------------------|---------------------------|)

[//]: # (| POST    | `/api/v1/products/request` | Create a loan request     |)

[//]: # (| PUT     | `/api/v1/products/topup`   | Topup an existing loan    |)

[//]: # (| PUT     | `/api/v1/products/repay`   | Repay a loan              |)

[//]: # (| DELETE  | `/api/v1/loans/clear`   | Clear old/defaulted loans |)

[//]: # (|         | `/api/v1/loans/x`       |                           |)

[//]: # (|         | `/api/v1/loans/x`       |                           |)

[//]: # (|         | `/api/v1/loans/x`       |                           |)

[//]: # (|         | `/api/v1/loans/x`       |                           |)



## Running the tests

```bash
./mvnw test
```

## Deployment

- Coming soon
## Built With

- [Spring](https://spring.io) - The web framework used

## Versioning

- Version 1(v1) of the API

- To view the swagger docs visit the below url after loading the project locally.
- Remember to change the port accordingly

```bash
http://localhost:8080/swagger-ui/index.html
```

## Authors

### Bill Kariri
