package by.shimakser.service;

import by.shimakser.security.jwt.JwtTokenProvider;
import by.shimakser.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Map<Object, Object> getJwtByUser(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        String token = jwtTokenProvider.createToken(user.getUsername());
        Map<Object, Object> response = new HashMap<>();
        response.put("name", user.getUsername());
        response.put("token", token);
        return response;
    }

    public String authenticateByString(String jwtToken, Model model) {
        if (!jwtTokenProvider.isValidateToken(jwtToken)) {
            return "login";
        }

        String username = jwtTokenProvider.getUsername(jwtToken);
        model.addAttribute("name", username);
        return "chat";
    }
}
