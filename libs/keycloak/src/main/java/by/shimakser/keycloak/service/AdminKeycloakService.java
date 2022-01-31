package by.shimakser.keycloak.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class AdminKeycloakService {

    private static final String KEYCLOAK_USERNAME = System.getenv("KEYCLOAK_USERNAME");
    private static final String KEYCLOAK_PASSWORD = System.getenv("KEYCLOAK_PASSWORD");
    private static final String KEYCLOAK_SERVER = System.getenv("KEYCLOAK_SERVER");
    private static final String KEYCLOAK_REALM = System.getenv("KEYCLOAK_REALM");
    private static final String KEYCLOAK_CLIENT = System.getenv("KEYCLOAK_CLIENT");

    private final Keycloak keycloak = Keycloak.getInstance(KEYCLOAK_SERVER,
            "master", KEYCLOAK_USERNAME, KEYCLOAK_PASSWORD, "admin-cli");

    public List<RoleRepresentation> getClientRoles() {
        RealmResource realm = keycloak.realm(KEYCLOAK_REALM);
        return realm.clients().findAll()
                .stream()
                .filter(clientRepresentation -> clientRepresentation.getClientId().equals(KEYCLOAK_CLIENT))
                .findAny()
                .map(ClientRepresentation::getId)
                .map(id -> realm.clients().get(id))
                .orElseThrow(NotFoundException::new)
                .roles()
                .list();
    }
}
