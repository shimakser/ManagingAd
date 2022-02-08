package by.shimakser.currencies.service;

import by.shimakser.currencies.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CurrenciesServiceTest {

    @Autowired
    CurrenciesService currenciesService;

    @Test
    void getCurrencies() {
        assertDoesNotThrow(() -> currenciesService.getCurrencies());
        assertNotNull(currenciesService.getCurrencies());
        assertEquals(currenciesService.getCurrencies().getClass(), ArrayList.class);
    }

    @Test
    void getCurrency() {
        String currencyId = "R01235";

        assertDoesNotThrow(() -> currenciesService.getCurrency(currencyId));
        assertNotNull(currenciesService.getCurrency(currencyId));
        assertEquals(currenciesService.getCurrency(currencyId).getClass(), Currency.class);
    }

    @Test
    void getCurrency_WithIncorrectId() {
        String currencyId = "R12345";

        assertThrows(NoSuchElementException.class, () -> currenciesService.getCurrency(currencyId));
    }
}