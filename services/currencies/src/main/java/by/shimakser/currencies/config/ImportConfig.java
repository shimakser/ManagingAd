package by.shimakser.currencies.config;

import by.shimakser.feign.config.MessageConverterConfig;
import by.shimakser.interceptor.config.InterceptorConfig;
import by.shimakser.security.config.SecurityResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({MessageConverterConfig.class, SecurityResourceConfig.class, InterceptorConfig.class})
@Configuration
public class ImportConfig {
}
