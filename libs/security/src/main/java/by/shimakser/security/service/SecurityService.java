package by.shimakser.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public boolean checkPrincipalAccess(String creator) {
        boolean isAdmin = getPrincipalRoles().contains("ADMIN");

        return isAdmin || creator.equals(getPrincipalName());
    }

    public String getPrincipalRoles() {
        return getPrincipal().getClaimAsMap("realm_access").get("roles").toString();
    }

    public String getPrincipalName() {
        return getPrincipal().getClaim("preferred_username").toString();
    }

    public Jwt getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) auth.getPrincipal();
    }
}
