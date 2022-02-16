package by.shimakser.currencies.service;

import by.shimakser.currencies.feign.CurrenciesFeignClient;
import by.shimakser.currencies.model.Currency;
import by.shimakser.currencies.repository.CurrenciesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @InjectMocks
    private CurrenciesService currenciesService;

    /**
     * {@link CurrenciesService#getCurrencies()}
     */
    @Test
    void Given_SearchAllCurrencies_When_GetCurrencies_Then_CheckIsCorrectlySearchedCurrencies() {
        // given
        Currency currency = new Currency();
        given(currenciesRepository.findAll()).willReturn(List.of(currency));

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
}