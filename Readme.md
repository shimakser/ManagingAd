<h1>Setting up and running</h1>
<h3>Environment variables, required to be specified in the service of the application in docker-compose file:</h3>

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
