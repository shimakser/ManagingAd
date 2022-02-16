package by.shimakser.keycloak.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    public boolean checkPrincipalAccess(String creator) {
        System.out.println(getPrincipalRoles());
        boolean isAdmin = getPrincipalRoles().toString().contains("ROLE_ADMIN");

        if (isAdmin || creator.equals(getPrincipalName())) {
            return true;
        }

        return false;
    }

    public List<GrantedAuthority> getPrincipalRoles() {
        return getAuthentication().getAuthorities()
                .stream()
                .filter(grantedAuthority -> grantedAuthority.toString().startsWith("ROLE_"))
                .collect(Collectors.toList());
    }

    public String getPrincipalName() {
        Map<String, Object> tokenAttributes = getJwtAuthenticationToken().getTokenAttributes();
        return tokenAttributes.get("preferred_username").toString();
    }

    private JwtAuthenticationToken getJwtAuthenticationToken() {
        Authentication auth = getAuthentication();
        return new JwtAuthenticationToken(
                (Jwt) auth.getPrincipal(),
                auth.getAuthorities());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
