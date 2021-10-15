package by.shimakser.security.authentication;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
