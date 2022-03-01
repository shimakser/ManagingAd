package by.shimakser.security.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
@PropertySource(value = {"classpath:application-security.yml"})
public class AdminKeycloakService {

    @Autowired
    private Keycloak keycloak;

    @Value("${spring.security.oauth2.client.provider.keycloak.realm}")
    private String keycloakRealm;
    @Value("${spring.security.oauth2.client.registration.managingad-app.client-id}")
    private String keycloakClient;

    public List<RoleRepresentation> getClientRoles() {
        RealmResource realm = keycloak.realm(keycloakRealm);
        return realm.clients().findAll()
                .stream()
                .filter(clientRepresentation -> clientRepresentation.getClientId().equals(keycloakClient))
                .findAny()
                .map(ClientRepresentation::getId)
                .map(id -> realm.clients().get(id))
                .orElseThrow(NotFoundException::new)
                .roles()
                .list();
    }
}
