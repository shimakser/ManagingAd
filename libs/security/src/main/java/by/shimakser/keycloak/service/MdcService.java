package by.shimakser.keycloak.service;

import org.slf4j.MDC;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class MdcService {

    public void setMdcPrincipal(Jwt principal) {
        MDC.put("id", principal.getId());
        MDC.put("username", principal.getClaim("preferred_username"));
        MDC.put("attribute", principal.getClaim("users_attribute"));
    }

    public void clearMdc() {
        MDC.clear();
    }
}
