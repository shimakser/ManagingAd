package by.shimakser.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JavaScriptMessageConverter extends AbstractJackson2HttpMessageConverter {

    public JavaScriptMessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().build(), new MediaType("application", "javascript"));
    }
}
