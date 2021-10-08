package by.shimakser.security;

import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import by.shimakser.security.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User userByUsername = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User by " + userEmail + " was not found"));

        return JwtUser.convertFromUser(userByUsername);
    }
}
