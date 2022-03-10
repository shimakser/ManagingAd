package by.shimakser.currencies.service;

import by.shimakser.currencies.mapper.CurrencyMapper;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.repository.CurrenciesRepository;
import by.shimakser.currencies.service.kafka.CurrencyKafkaService;
import by.shimakser.dto.Currencies;
import by.shimakser.dto.CurrencyDto;
import by.shimakser.feign.client.CurrenciesFeignClient;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@SpringBootTest
class CurrenciesServiceTest {

    @Mock
    private CurrenciesRepository currenciesRepository;
    @Mock
    private CurrenciesFeignClient currenciesFeignClient;
    @Mock
    private CurrencyMapper currencyMapper;
    @Mock
    private CurrencyKafkaService currencyKafkaService;

    @InjectMocks
    private CurrenciesService currenciesService;

    private static final CurrencyDto CURRENCY_DTO = new CurrencyDto("R01235", "840", "USD", "1",
            "Доллар США", "75.3", "75.6", LocalDateTime.now());
    private static final Currency CURRENCY = new Currency("R01235", "840", "USD", "1",
            "Доллар США", "75.3", "75.6", LocalDateTime.now());
    private static final String CURRENCY_ID = "R01235";
    private static final Currencies CURRENCIES = new Currencies("2022-02-09", "2022-02-08",
            "www.cbr-xml-daily.ru", "2022-02-08", Map.of("USD", CURRENCY_DTO));
    private static final Currencies EMPTY_CURRENCIES = new Currencies();

    /**
     * {@link CurrenciesService#getCurrencies()}
     */
    @Test
    void Given_SearchAllCurrencies_When_GetCurrencies_Then_CheckIsCorrectlySearchedCurrencies() {
        // given
        Currency currency = new Currency();
        given(currenciesRepository.findAll()).willReturn(List.of(currency));
        given(currencyMapper.mapToEntity(CURRENCY_DTO)).willReturn(CURRENCY);

        // when
        List<Currency> currencies = currenciesService.getCurrencies();

        // then
        then(currenciesRepository)
                .should()
                .findAll();
        then(currenciesFeignClient)
                .should(never())
                .getCurrencies();
        assertEquals(currencies, List.of(currency));
    }

    /**
     * {@link CurrenciesService#getListEntity(Currencies)}
     */
    @Test
    void When_GetCurrencyList_Then_CheckIsCorrectlySearchedCurrencies() {
        // given
        given(currencyMapper.mapToEntity(CURRENCY_DTO)).willReturn(CURRENCY);

        // when
        List<Currency> currenciesList = currenciesService.getListEntity(CURRENCIES);

        // then
        assertEquals(CURRENCY, currenciesList.get(0));
    }

    /**
     * {@link CurrenciesService#getListEntity(Currencies)}
     */
    @Test
    void When_GetCurrencyListByEmptyCurrencies_Then_CheckIsMethodReturnEmptyList() {
        // when
        List<Currency> currenciesList = currenciesService.getListEntity(EMPTY_CURRENCIES);

        // then
        assertEquals(Collections.emptyList(), currenciesList);
    }

    /**
     * {@link CurrenciesService#getEntity(Currencies, String)}
     */
    @Test
    void When_GetCurrencyByIdFromCurrencies_Then_CheckIsCorrectlySearchedCurrency() {
        // given
        given(currencyMapper.mapToEntity(CURRENCY_DTO)).willReturn(CURRENCY);

        // when
        Currency currency = currenciesService.getEntity(CURRENCIES, CURRENCY_ID);

        // then
        assertEquals(CURRENCY, currency);
    }

    /**
     * {@link CurrenciesService#getEntity(Currencies, String)}
     */
    @Test
    void When_GetCurrencyByIdFromEmptyCurrencies_Then_CatchException() {
        // when
        BDDCatchException.when(() ->currenciesService.getEntity(EMPTY_CURRENCIES, CURRENCY_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * {@link CurrenciesService#getEntity(Currencies, String)}
     */
    @Test
    void Given_SetIncorrectId_When_GetCurrencyByIdFromCurrencies_Then_CatchException() {
        // given
        String incorrectId = "R12345";
        given(currencyMapper.mapToEntity(CURRENCY_DTO)).willReturn(CURRENCY);

        // when
        BDDCatchException.when(() ->currenciesService.getEntity(CURRENCIES, incorrectId));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(NoSuchElementException.class);
    }
}