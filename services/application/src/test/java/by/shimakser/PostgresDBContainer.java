package by.shimakser;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresDBContainer extends PostgreSQLContainer<PostgresDBContainer> {

    private static final String POSTGRES_IMAGE_VERSION = "postgres:14";

    private static PostgresDBContainer container;

    private PostgresDBContainer() {
        super(POSTGRES_IMAGE_VERSION);
    }

    public static PostgresDBContainer getInstance() {
        if (container == null) {
            container = new PostgresDBContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("POSTGRES_URL", container.getJdbcUrl());
        System.setProperty("POSTGRES_USERNAME", container.getUsername());
        System.setProperty("POSTGRES_PASSWORD", container.getPassword());
    }
}
