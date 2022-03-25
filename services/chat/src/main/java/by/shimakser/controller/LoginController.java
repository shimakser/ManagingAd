package by.shimakser.controller;

import by.shimakser.model.User;
import by.shimakser.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;

@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> getJwt(@RequestBody User request) throws EntityNotFoundException {
        try {
            return ResponseEntity.ok(loginService.getJwtByUser(request));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid name/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(@RequestParam String token, Model model) {

        return loginService.authenticateByString(token, model);
    }
}
