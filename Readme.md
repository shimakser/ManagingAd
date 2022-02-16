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
    - KEYCLOAK_CLIENT,
    - KEYCLOAK_SERVER,
    - KEYCLOAK_SECRET,
    - KEYCLOAK_USERNAME,
    - KEYCLOAK_PASSWORD.
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