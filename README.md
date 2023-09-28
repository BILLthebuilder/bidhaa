# bidhaa

[//]: # ([![bidhaa app]&#40;https://github.com/BILLthebuilder/bidhaa/actions/workflows/aws.yml/badge.svg&#41;]&#40;https://github.com/BILLthebuilder/bidhaa/actions/workflows/aws.yml&#41;)
## About

This is an Inventory management system that has the following features:

- Create,Read,Update and Delete(CRUD) users
- Create,Read,Update and Delete(CRUD) products
- Asynchronous file upload with Kafka
- Audit/logging of requests with Kafka
- Access Control Based on roles and privilleges(permissions)

This app is built using Java(Spring Boot)
Asynchronous tasks are handled by Kafka


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

`cd bidhaa`

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

| Request | Endpoint                                     | Function                  |
|---------|----------------------------------------------|---------------------------|
| POST    | `/api/v1/users/create`                       | Create a new user         |
| POST    | `/api/v1/users/login`                        | Login a registered user   |
| GET     | `/api/v1/users?page=?&size=?&sort=?&order=?` | Get  all registered users |
| PUT     | `/api/v1/users/{Id}`                         | Update a registered user  |
| DELETE  | `/api/v1/users/{Id}`                         | Delete a registered user  |

#### Product endpoints


| Request | Endpoint                                        | Function             |
|---------|-------------------------------------------------|----------------------|
| POST    | `/api/v1/products/create`                       | Create a new Product |
| POST    | `/api/v1/products/upload`                       | Upload products csv  |
| GET     | `/api/v1/products?page=?&size=?&sort=?&order=?` | Get all products     |
| GET     | `/api/v1/products/{Id}`                         | Get a product by ID  |
| PUT     | `/api/v1/products/{Id}`                         | Update a product     |
| DELETE  | `/api/v1/products/{Id}`                         | Delete a product     |



## Running the tests

```bash
./mvnw test
```

## Deployment

- Coming soon
## Built With

- [Spring](https://spring.io) - The web framework used
- [Kafka](https://kafka.apache.org) - Event streaming/queuing 

## Versioning

- Version 1(v1) of the API

- To view the swagger docs visit the below url after loading the project locally.
- Remember to change the port accordingly

```bash
http://localhost:8080/swagger-ui/index.html
```
- The Open Api V3 spec document can be viewed and retrieved from the below url
- Remember to change the port accordingly
```bash
http://localhost:8080/v3/api-docs
```
## Authors

### Bill Kariri
