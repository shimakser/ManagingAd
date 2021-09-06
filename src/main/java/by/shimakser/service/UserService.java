package by.shimakser.service;

import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByName = userRepository.findByUsername(username);
        if (!userByName.isPresent()) {
            throw new UsernameNotFoundException(username + " was not found");
        }
        return new org.springframework.security.core.userdetails.User(
                userByName.get().getUsername(),
                userByName.get().getPassword(),
                AuthorityUtils.createAuthorityList(userByName.get().getRole().toString())
        );
    }

    public void add(User user) {
        Optional<User> userFromDBByEmail = userRepository.findByUserEmail(user.getUserEmail());

        if (!userFromDBByEmail.isPresent()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }

    public User get(Long id) {
        Optional<User> userById = userRepository.findById(id);
        return userById.get();
    }

    public Page<User> getAll(Optional<Integer> page,
                             Optional<Integer> size,
                             Optional<String> sortBy
    ) {
        return userRepository.findAll(
                PageRequest.of(page.orElse(0),
                        size.orElse(userRepository.findAll().size()),
                        Sort.Direction.ASC, sortBy.orElse("id"))
        );
    }

    public void update(Long id, User newUser) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            newUser.setId(id);
            newUser.setPassword(bCryptPasswordEncoder.encode(userById.get().getPassword()));
            userRepository.save(newUser);
        }
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
