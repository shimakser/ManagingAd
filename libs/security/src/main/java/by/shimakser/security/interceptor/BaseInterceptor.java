package by.shimakser.security.interceptor;

import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class BaseInterceptor {

    private static final String USER_ID = "id";
    private static final String USERNAME = "username";
    private static final String ATTRIBUTE = "attribute";

    private final String TRACE_ID = "X-REQUEST-ID";
    private static final String OPERATION_ID = "operationId";
    private static final String PARENT_OPERATION_ID = "parentOperationId";

    public Map<String, String> setMdcAndHeader(String traceId) {
        Map<String, String> attributeMap = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) auth.getPrincipal();

        attributeMap.put(USER_ID, principal.getId());
        attributeMap.put(USERNAME, principal.getClaim("preferred_username"));
        attributeMap.put(ATTRIBUTE, principal.getClaim("users_attribute"));

        attributeMap.put(TRACE_ID, traceId);
        attributeMap.put(OPERATION_ID, getRandomNumber());
        attributeMap.put(PARENT_OPERATION_ID, "null");

        MDC.setContextMap(attributeMap);
        return attributeMap;
    }

    public void clearMdc() {
        MDC.clear();
    }

    public String getRandomNumber() {
        int value = (int) (Math.random() * ((100 - 1) + 1));
        return String.valueOf(value);
    }
}
