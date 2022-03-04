package by.shimakser.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class HttpInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, String> attributeMap = new HashMap<>();

        logPrincipal(request, attributeMap);
        logOperationIds(request, attributeMap);

        MDC.setContextMap(attributeMap);
        log.info("Intercept http request.");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
    }

    private void logPrincipal(HttpServletRequest request, Map<String, String> map) {
        if (request.getHeader(USER_ID) == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) auth.getPrincipal();

            map.put(USER_ID, jwt.getId());
            map.put(USERNAME, jwt.getClaim(USERNAME_KEY));
            map.put(USER_ATTRIBUTE, jwt.getClaim(USER_ATTRIBUTE_KEY));
        } else {
            map.put(USER_ID, request.getHeader(USER_ID));
            map.put(USERNAME, request.getHeader(USERNAME));
            map.put(USER_ATTRIBUTE, request.getHeader(USER_ATTRIBUTE));
        }
    }

    private void logOperationIds(HttpServletRequest request, Map<String, String> map) {
        String traceId = Optional.ofNullable(request.getHeader(TRACE_ID))
                .orElseGet(super::getRandomNumber);

        if (request.getHeader(OPERATION_ID) == null) {
            map.put(TRACE_ID, traceId);
            map.put(OPERATION_ID, super.getRandomNumber());
            map.put(PARENT_OPERATION_ID, "null");
        } else {
            map.put(TRACE_ID, request.getHeader(TRACE_ID));
            map.put(PARENT_OPERATION_ID, request.getHeader(OPERATION_ID));
            map.put(OPERATION_ID, super.getRandomNumber());
        }
    }

    public void clearMdc() {
        MDC.clear();
    }
}
