# Setting up and running
### Environment variables, required to be specified in the service of the application in docker-compose file:

- DB - title of the database.
- PostgreSQL:
  - POSTGRES_USER,
  - POSTGRES_PASSWORD,
  - POSTGRES_HOST - name of the postgres service,
  - POSTGRES_PORT.
- MongoDB:
  - MONGO_HOST - name of the mongo service,
  - MONGO_PORT.
- Kafka:
  - KAFKA_HOST - name of the kafka service,
  - KAFKA_PORT.


### Running app in docker:
From project directory in the terminal execute the next commands:

- ./mvnw clean package</u> - for packaging project,
- docker-compose up</u> for running in docker.


# About application
### Study project with study tasks. Rest api for the managing advertisers and their advertising campaigns, splitted for some step:
1. branch dev-shiyanov — application based Spring Boot, with PostgreSQL, Spring Data Jpa, 
Flyway, Basic authentication by Spring Security, Pagination and soft delete.
2. branch dev-shiyanov-training — main branch, where to the application from dev-shiyanov added @Transactional, 
handling exception by @ControllerAdvice, dto with using Mapstruct, filter Campaign-entity by Spring Data Specification,  
import/export from .csv file and database. 

    And also branch include result of work from next branches:

   - dev-shiyanov-jwt — added auth with JWT.
   - dev-shiyanov-feign — added Spring Cloud OpenFeign and calling external api with it.
   - dev-shiyanov-kafka — launched application in the docker, added TestContainers and working with Apache Kafka.
   - dev-shiyanov-jooq — added filter Campaign-entity by jooq.
   - dev-shiyanov-cache — added caffeine-cache and wrote a custom cache, wich save their data to mongodb.
   - dev-shiyanov-elk — added ELK stack to the application.

