package by.shimakser.office.config;

import by.shimakser.interceptor.config.InterceptorConfig;
import by.shimakser.security.config.SecurityResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SecurityResourceConfig.class, InterceptorConfig.class})
public class ImportConfig {
}
