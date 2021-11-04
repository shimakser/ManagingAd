package by.shimakser.jooq.config;

import lombok.SneakyThrows;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class JooqGeneratedConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.jooq.generator.package-name}")
    private String targetPackageName;

    @Value("${spring.jooq.generator.target-directory}")
    private String targetDirectory;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new GenerationTool().run(configureGenerator());
    }

    private Configuration configureGenerator() {
        return new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver(driver)
                        .withUrl(url)
                        .withUser(username)
                        .withPassword(password))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withInputSchema("public")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history"))
                        .withTarget(new Target()
                                .withPackageName(targetPackageName)
                                .withDirectory(targetDirectory)));
    }
}
