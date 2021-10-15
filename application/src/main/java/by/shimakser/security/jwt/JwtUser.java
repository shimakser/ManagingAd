package by.shimakser.security.jwt;

import by.shimakser.model.ad.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String userEmail;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean userDeleted;

    public static UserDetails convertFromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(), user.getPassword(),
                user.getUserRole().getAuthorities()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !userDeleted;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
