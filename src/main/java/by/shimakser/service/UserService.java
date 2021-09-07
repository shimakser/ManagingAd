package by.shimakser.service;

import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Optional<User>> get(Long id) {
        Optional<User> userById = userRepository.findById(id);
        return Stream.of(userById).filter(u -> u.get().isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
    }

    public List<User> getAll(Optional<Integer> page,
                             Optional<Integer> size,
                             Optional<String> sortBy
    ) {
        return userRepository.findAll(
                PageRequest.of(page.orElse(0),
                        size.orElse(userRepository.findAll().size()),
                        Sort.Direction.ASC, sortBy.orElse("id")))
                .stream().filter(user -> user.isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
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
        Optional<User> userById = userRepository.findById(id);
        userById.get().setUserDeleted(Boolean.TRUE);
        userRepository.save(userById.get());
    }

    public User getDeletedUser(Long id) {
        Optional<User> deletedUserById = Optional.of(userRepository.findByIdAndUserDeletedTrue(id));
        return deletedUserById.get();
    }

    public List<User> getDeletedUsers() {
        List<User> deletedAllUsersById = userRepository.findAllByUserDeletedTrue();
        return deletedAllUsersById;
    }
}
