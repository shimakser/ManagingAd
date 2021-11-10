package by.shimakser.repository.mongo;

import by.shimakser.model.office.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends MongoRepository<Currency, Long> {
}
