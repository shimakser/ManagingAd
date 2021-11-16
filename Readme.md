<h1>Setting up and running</h1>
<h3>Environment variables, required to be specified in the service of the application
in docker-compose file:</h3>

<ul>
    <li>DB - title of the database.</li>
    <li>PostgreSQL:
       <ul>
            <li>POSTGRES_USER,</li>
            <li>POSTGRES_PASSWORD,</li>
            <li>POSTGRES_HOST - name of the postgres service,</li>
            <li>POSTGRES_PORT,</li>
        </ul> 
    </li>
    <li>MongoDB:
       <ul>
            <li>MONGO_HOST - name of the mongo service,</li>
            <li>MONGO_PORT,</li>
        </ul> 
    </li>
    <li>Kafka:
       <ul>
            <li>KAFKA_HOST - name of the kafka service,</li>
            <li>KAFKA_PORT,</li>
        </ul> 
    </li>
</ul>

<h3>Running app in docker:</h3>
<p>From project directory in the terminal execute the next commands:
<ul>
    <li><u>./mvnw clean package</u> - for packaging project,</li>
    <li><u>docker-compose up</u> for running in docker.</li>
</ul>

<hr>
<h1>About application</h1>
<h5>Study project with study tasks. Rest api for the managing advertisers and their advertising campaigns, 
splitted for some step:</h5>
<p>- branch <u>dev-shiyanov</u>: application based Spring Boot, with PostgreSQL, Spring Data Jpa, 
Flyway, Basic authentication by Spring Security, Pagination and soft delete.</p>
<p>- branch <u>dev-shiyanov-training</u>: main branch, where to the application from <u>dev-shiyanov</u> added @Transactional, 
handling exception by @ControllerAdvice, dto with using Mapstruct, filter Campaign-entity by Spring Data Specification,  
import/export from .csv file and database.
<p>And also branch include result of work from next branches:</p>
    <ul>
        <li><u>dev-shiyanov-jwt</u>: added auth with JWT.</li>
        <li><u>dev-shiyanov-feign</u>: added Spring Cloud OpenFeign and calling external api with it.</li>
        <li><u>dev-shiyanov-kafka</u>: launched application in the docker, added TestContainers and working with Apache Kafka.</li>
        <li><u>dev-shiyanov-jooq</u>:</li> added filter Campaign-entity by jooq.
        <li><u>dev-shiyanov-cache</u>: added caffeine-cache and wrote a custom cache, wich save their data to mongodb.</li>
        <li><u>dev-shiyanov-elk</u>: added ELK stack to the application.</li>
    </ul>
