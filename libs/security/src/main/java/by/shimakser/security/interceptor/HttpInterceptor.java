package by.shimakser.security.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class HttpInterceptor extends BaseInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = Optional.ofNullable(request.getHeader(super.getTRACE_ID()))
                .orElseGet(super::getRandomNumber);

        super.setMdcAndHeader(traceId).forEach(response::setHeader);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        super.clearMdc();
    }
}
