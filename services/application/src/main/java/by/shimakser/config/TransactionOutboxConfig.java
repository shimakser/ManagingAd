package by.shimakser.config;

import com.gruelbox.transactionoutbox.*;
import org.springframework.context.annotation.*;

import java.util.Set;

@Configuration
@Import(SpringTransactionOutboxConfiguration.class)
public class TransactionOutboxConfig {

    @Bean
    @Lazy
    public TransactionOutbox transactionOutbox(SpringTransactionManager springTransactionManager,
                                               SpringInstantiator springInstantiator,
                                               Persistor customPersistor) {
        return TransactionOutbox.builder()
                .instantiator(springInstantiator)
                .transactionManager(springTransactionManager)
                .persistor(customPersistor)
                .build();
    }

    @Bean
    @Primary
    public Persistor customPersistor() {
        return new CustomPersistor(
                Dialect.POSTGRESQL_9,
                false,
                buildInvocationSerializer()
        );
    }

    private InvocationSerializer buildInvocationSerializer() {
        Set<Class<?>> classes = Set.of(String.class, Long.class, Double.class, Integer.class,
                Enum.class, Float.class, Short.class, Byte.class);
        return DefaultInvocationSerializer
                .builder()
                .serializableTypes(classes)
                .build();
    }
}
