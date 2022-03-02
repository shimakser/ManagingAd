package by.shimakser.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FeignInterceptor extends BaseInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        setHeaderToMdc()
                .forEach((key, value) -> template.header(key, List.of(value)));

        log.info("Intercept feign request.");
    }

    private Map<String, String> setHeaderToMdc() {
        Map<String, String> mdcHeaders = MDC.getCopyOfContextMap();

        mdcHeaders.put(PARENT_OPERATION_ID, mdcHeaders.get(OPERATION_ID));
        mdcHeaders.put(OPERATION_ID, super.getRandomNumber());

        MDC.setContextMap(mdcHeaders);
        return mdcHeaders;
    }
}
