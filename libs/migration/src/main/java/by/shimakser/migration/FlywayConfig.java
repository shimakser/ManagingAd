package by.shimakser.migration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    private static final String MIGRATIONS_LOCATION = "db/external/migration";
    private static final String FLYWAY_TABLE = "flyway_schema_history_external";

    private final DataSource dataSource;

    @Autowired
    public FlywayConfig(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(initMethod = "migrate")
    public Flyway httpIdempotencyFlyway() {
        return Flyway
                .configure()
                .dataSource(dataSource)
                .locations(MIGRATIONS_LOCATION)
                .table(FLYWAY_TABLE)
                .baselineOnMigrate(true)
                .baselineVersion("0.0.0")
                .load();
    }

}
