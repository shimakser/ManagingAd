package by.shimakser.currencies.repository;

import by.shimakser.currencies.model.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrenciesRepository extends MongoRepository<Currency, Long> {
}
