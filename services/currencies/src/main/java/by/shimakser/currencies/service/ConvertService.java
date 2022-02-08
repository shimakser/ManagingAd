package by.shimakser.currencies.service;

import by.shimakser.currencies.model.Currencies;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.model.CurrencyCode;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ConvertService {

    public List<Currency> getListEntity(Currencies currencies) {
        return currencies.getValute() == null
                ? Collections.emptyList()
                : Arrays.stream(CurrencyCode.values())
                .map(code -> currencies.getValute().get(code.toString()))
                .collect(Collectors.toList());
    }

    public Currency getEntity(Currencies currencies, String id) {
        if (currencies.getValute() == null) {
            throw new NoSuchElementException("Currencies valute list is empty.");
        }
        return currencies.getValute().values()
                .stream()
                .filter(currency -> currency.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Incorrect id: " + id));
    }
}
