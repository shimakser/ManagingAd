package by.shimakser.redis.config;

import by.shimakser.migration.FlywayConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(FlywayConfig.class)
@Configuration
public class FlywayImportConfig {
}
