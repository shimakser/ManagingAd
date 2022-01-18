package by.shimakser.redis.repository;

import by.shimakser.redis.model.CurrencyTranslation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRepository extends MongoRepository<CurrencyTranslation, Long> {

    Optional<CurrencyTranslation> findCurrencyTranslationByCharCode(String charCode);
}
