package by.shimakser.repository.currency;


import by.shimakser.model.currency.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends MongoRepository<Currency, Long> {
}
