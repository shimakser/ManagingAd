package by.shimakser.currencies.service;

import by.shimakser.currencies.model.Currencies;
import by.shimakser.currencies.model.Currency;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ConvertServiceTest {

    @Autowired
    private ConvertService convertService;

    private static final Currency CURRENCY = new Currency("R01235", "840", "USD", "1",
            "Доллар США", "75.3", "75.6", LocalDateTime.now());
    private static final String CURRENCY_ID = "R01235";
    private static final Currencies CURRENCIES = new Currencies("2022-02-09", "2022-02-08",
            "www.cbr-xml-daily.ru", "2022-02-08", Map.of("USD", CURRENCY));
    private static final Currencies EMPTY_CURRENCIES = new Currencies();

    /**
     * {@link ConvertService#getListEntity(Currencies)}
     */
    @Test
    void When_GetCurrencyList_Then_CheckIsCorrectlySearchedCurrencies() {
        // when
        List<Currency> currenciesList = convertService.getListEntity(CURRENCIES);

        // then
        assertEquals(CURRENCY, currenciesList.get(0));
    }

    /**
     * {@link ConvertService#getListEntity(Currencies)}
     */
    @Test
    void When_GetCurrencyListByEmptyCurrencies_Then_CheckIsMethodReturnEmptyList() {
        // when
        List<Currency> currenciesList = convertService.getListEntity(EMPTY_CURRENCIES);

        // then
        assertEquals(Collections.emptyList(), currenciesList);
    }

    /**
     * {@link ConvertService#getEntity(Currencies, String)}
     */
    @Test
    void When_GetCurrencyByIdFromCurrencies_Then_CheckIsCorrectlySearchedCurrency() {
        // when
        Currency currency = convertService.getEntity(CURRENCIES, CURRENCY_ID);

        // then
        assertEquals(currency, CURRENCY);
    }

    /**
     * {@link ConvertService#getEntity(Currencies, String)}
     */
    @Test
    void When_GetCurrencyByIdFromEmptyCurrencies_Then_CatchException() {
        // when
        BDDCatchException.when(() ->convertService.getEntity(EMPTY_CURRENCIES, CURRENCY_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * {@link ConvertService#getEntity(Currencies, String)}
     */
    @Test
    void Given_SetIncorrectId_When_GetCurrencyByIdFromCurrencies_Then_CatchException() {
        // given
        String incorrectId = "R12345";

        // when
        BDDCatchException.when(() ->convertService.getEntity(CURRENCIES, incorrectId));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(NoSuchElementException.class);
    }
}