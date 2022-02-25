package by.shimakser.security.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FeignInterceptor extends BaseInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        MDC.getCopyOfContextMap()
                .forEach((key, value) -> template.header(key, List.of(value)));

        log.info("Intercept feign request to path {}", template.path());
    }
}
