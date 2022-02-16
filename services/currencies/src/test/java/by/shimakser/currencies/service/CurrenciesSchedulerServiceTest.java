package by.shimakser.currencies.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.atLeast;
import static org.mockito.BDDMockito.verify;

@SpringBootTest
class CurrenciesSchedulerServiceTest {

    @SpyBean
    private CurrenciesSchedulerService currenciesSchedulerService;

    /**
     * {@link CurrenciesSchedulerService#schedulerCurrencies()}
     */
    @Test
    void When_WaitBeforeStartingScheduling_Then_CheckIsSchedulingWentNeededTimes() {
        await()
                .atMost(1, TimeUnit.HOURS)
                .untilAsserted(() -> verify(currenciesSchedulerService, atLeast(1))
                        .schedulerCurrencies());
    }
}