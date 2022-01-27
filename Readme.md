# Setting up and running
### Environment variables, required to be specified in the service of the applications in docker-compose file:

- PORT - application port;
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
- KeyCloak:
    - KEYCLOAK_REALM,
    - KEYCLOAK_RESOURCE,
    - KEYCLOAK_SERVER,
    - KEYCLOAK_SECRET.
- RabbitMQ:
    - RABBITMQ_HOST,
    - RABBITMQ_PORT,
    - RABBITMQ_USERNAME,
    - RABBITMQ_PASSWORD.
- Redis:
    - REDIS_HOST,
    - REDIS_PORT.


### Running app in docker:
From project directory in the terminal execute the next commands:

- ./mvnw clean package</u> - for packaging project,
- docker-compose up</u> for running in docker.


# About application
### Study project with study tasks. Rest api for the managing advertisers and their advertising campaigns, splitted for some step:
1. branch dev-test — application based Spring Boot, with PostgreSQL, Spring Data Jpa, 
Flyway, Basic authentication by Spring Security, Pagination and soft delete.
2. branch dev-training — main branch, where to the application from dev-test added @Transactional, 
handling exception by @ControllerAdvice, dto with using Mapstruct, filter Campaign-entity by Spring Data Specification,  
import/export from .csv file and database. 

    And also branch include result of work from next branches:

   - dev-training-jwt — added auth with JWT.
   - dev-training-feign — added Spring Cloud OpenFeign and calling external api with it.
   - dev-training-kafka — launched application in the docker, added TestContainers and working with Apache Kafka.
   - dev-training-jooq — added filter Campaign-entity by jooq.
   - dev-training-cache — added caffeine-cache and wrote a custom cache, witch save their data to mongodb.
   - dev-training-elk — added ELK stack to the application.
   - dev-training-poi — wrote exports date from db to .xls and .pdf with help Apache POI and iTextPdf.
   - dev-training-rabbitmq - added sending currencies to rabbitmq.
   - dev-training-redis - wrote service for caching into redis by name and field-translation
   - dev-training-keycloak - added authorization by KeyCloak.

