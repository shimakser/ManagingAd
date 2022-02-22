package by.shimakser.security.controller;

import by.shimakser.security.service.AdminKeycloakService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/keycloak")
public class AdminController {

    private final AdminKeycloakService adminKeycloakService;

    @Autowired
    public AdminController(AdminKeycloakService adminKeycloakService) {
        this.adminKeycloakService = adminKeycloakService;
    }

    @GetMapping("/roles")
    public List<RoleRepresentation> getClientRoles() {
        return adminKeycloakService.getClientRoles();
    }
}
