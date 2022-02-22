package by.shimakser.security.interceptor;

import by.shimakser.security.service.AttributeService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Autowired
    private AttributeService attributeService;

    @Override
    public void apply(RequestTemplate template) {
        List<String> traces = new ArrayList<>(template.headers()
                .getOrDefault(attributeService.getTRACE_ID(), List.of(attributeService.getRandomNumber())));

        attributeService.setMdcAndHeader(traces.get(0))
                .forEach((key, value) -> template.header(key, List.of(value)));
    }
}
