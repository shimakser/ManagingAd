package by.shimakser.currencies.service;

import by.shimakser.currencies.model.Currencies;
import by.shimakser.currencies.model.Currency;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConvertServiceTest {

    @Autowired
    ConvertService convertService;

    private static Currencies currencies;
    private static Currencies emptyCurrencies;
    private static Currency currency;
    private static String currencyId;

    @BeforeAll
    public static void init() {
        currency = new Currency("R01235", "840", "USD", "1",
                "Доллар США", "75.3", "75.6", LocalDateTime.now());

        Map<String, Currency> valute = new HashMap<>();
        valute.put("USD", currency);
        currencies = new Currencies("2022-02-09", "2022-02-08",
                "www.cbr-xml-daily.ru", "2022-02-08",
                valute);

        emptyCurrencies = new Currencies();

        currencyId = "R01235";
    }

    @Test
    void getListEntity() {
        assertDoesNotThrow(() -> convertService.getListEntity(currencies));
        assertNotNull(convertService.getListEntity(currencies));
        assertEquals(currency, convertService.getListEntity(currencies).get(0));
    }

    @Test
    void getListEntity_WithEmptyCurrenciesEntity() {
        assertDoesNotThrow(() -> convertService.getListEntity(emptyCurrencies));
        assertNotNull(convertService.getListEntity(emptyCurrencies));
        assertEquals(Collections.emptyList(), convertService.getListEntity(emptyCurrencies));
    }

    @Test
    void getEntity() {
        assertDoesNotThrow(() -> convertService.getEntity(currencies, currencyId));
        assertNotNull(convertService.getEntity(currencies, currencyId));
        assertEquals(currency, convertService.getEntity(currencies, currencyId));
    }

    @Test
    void getEntity_WithEmptyCurrenciesEntity() {
        assertThrows(NoSuchElementException.class, () -> convertService.getEntity(emptyCurrencies, currencyId));
    }

    @Test
    void getEntity_WithIncorrectId() {
        assertThrows(NoSuchElementException.class, () -> convertService.getEntity(currencies, "R12345"));
    }
}