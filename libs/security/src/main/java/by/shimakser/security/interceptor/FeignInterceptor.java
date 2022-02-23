package by.shimakser.security.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FeignInterceptor extends BaseInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        List<String> traceHeaders = new ArrayList<>(template.headers()
                .getOrDefault(super.getTRACE_ID(), List.of(super.getRandomNumber())));

        super.setMdcAndHeader(traceHeaders.get(0))
                .forEach((key, value) -> template.header(key, List.of(value)));
    }
}
