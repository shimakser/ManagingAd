package by.shimakser.redis.config;

import by.shimakser.redis.model.CurrencyTranslation;
import by.shimakser.redis.repository.TranslationRepository;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

import java.util.List;

@ChangeLog
public class MongoChangeLog {

    @ChangeSet(order = "001", id = "USD", author = "testAuthor", runAlways = true)
    public void someChange1(TranslationRepository repository) {
        List<CurrencyTranslation> translations = List.of(
                new CurrencyTranslation(1L, "USD", "Доллар США"),
                new CurrencyTranslation(2L, "BYN", "Беларусский рубль")
        );
        repository.saveAll(translations);
    }
}
