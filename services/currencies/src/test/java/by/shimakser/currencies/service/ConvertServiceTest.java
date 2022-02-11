package by.shimakser.currencies.service;

import by.shimakser.currencies.model.Currencies;
import by.shimakser.currencies.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConvertServiceTest {

    @Autowired
    private ConvertService convertService;

    private static final Currencies CURRENCIES = new Currencies("2022-02-09", "2022-02-08",
            "www.cbr-xml-daily.ru", "2022-02-08", new HashMap<>());
    private static final Currencies EMPTY_CURRENCIES = new Currencies();
    private static final Currency CURRENCY = new Currency("R01235", "840", "USD", "1",
            "Доллар США", "75.3", "75.6", LocalDateTime.now());
    private static final String CURRENCY_ID = "R01235";

    @Test
    void getListEntity() {
        Map<String, Currency> valute = new HashMap<>();
        valute.put("USD", CURRENCY);
        CURRENCIES.setValute(valute);

        List<Currency> currenciesList = convertService.getListEntity(CURRENCIES);

        assertEquals(CURRENCY, currenciesList.get(0));
    }

    @Test
    void getListEntity_WithEmptyCurrenciesEntity() {
        List<Currency> currenciesList = convertService.getListEntity(EMPTY_CURRENCIES);

        assertEquals(Collections.emptyList(), currenciesList);
    }

    @Test
    void getEntity() {
        Currency currency = convertService.getEntity(CURRENCIES, CURRENCY_ID);

        assertEquals(currency, convertService.getEntity(CURRENCIES, CURRENCY_ID));
    }

    @Test
    void getEntity_WithEmptyCurrenciesEntity() {
        assertThrows(NoSuchElementException.class, () -> convertService.getEntity(EMPTY_CURRENCIES, CURRENCY_ID));
    }

    @Test
    void getEntity_WithIncorrectId() {
        assertThrows(NoSuchElementException.class, () -> convertService.getEntity(CURRENCIES, "R12345"));
    }
}