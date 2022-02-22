package by.shimakser.security.interceptor;

import by.shimakser.security.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    @Autowired
    private AttributeService attributeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = Optional.ofNullable(request.getHeader(attributeService.getTRACE_ID())).orElseGet(attributeService::getRandomNumber);

        attributeService.setMdcAndHeader(traceId).forEach(response::setHeader);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        attributeService.clearMdc();
    }
}
