package by.shimakser.security.interceptor;

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

@Component
public class HttpInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = Optional.ofNullable(request.getHeader(TRACE_ID))
                .orElseGet(super::getRandomNumber);

        setMdc(traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
    }

    public void setMdc(String traceId) {
        Map<String, String> attributeMap = new HashMap<>();

        attributeMap.put(USER_ID, getPrincipal().getId());
        attributeMap.put(USERNAME, getPrincipal().getClaim(USERNAME_KEY));
        attributeMap.put(USER_ATTRIBUTE, getPrincipal().getClaim(USER_ATTRIBUTE_KEY));

        attributeMap.put(TRACE_ID, traceId);
        attributeMap.put(OPERATION_ID, super.getRandomNumber());
        attributeMap.put(PARENT_OPERATION_ID, "null");

        MDC.setContextMap(attributeMap);
    }

    private Jwt getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) auth.getPrincipal();
    }

    public void clearMdc() {
        MDC.clear();
    }
}
