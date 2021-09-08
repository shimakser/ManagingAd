package by.shimakser.security;

import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByUsername = Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username + " was not found")));
        return new org.springframework.security.core.userdetails.User(
                userByUsername.get().getUsername(),
                userByUsername.get().getPassword(),
                AuthorityUtils.createAuthorityList(userByUsername.get().getUserRole().toString()));
    }
}
