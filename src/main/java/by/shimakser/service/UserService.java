package by.shimakser.service;

import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<HttpStatus> add(User user) {
        Optional<User> userFromDBByEmail = userRepository.findByUserEmail(user.getUserEmail());

        if (userFromDBByEmail.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(Role.USER);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<List<User>> get(Long id) {
        Optional<User> userById = userRepository.findById(id);
        List<User> user = Stream.of(userById.get()).filter(u -> u.isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getAll(Optional<Integer> page,
                                             Optional<Integer> size,
                                             Optional<String> sortBy
    ) {
        List<User> users = userRepository.findAll(
                        PageRequest.of(page.orElse(0),
                                size.orElse(userRepository.findAll().size()),
                                Sort.Direction.ASC, sortBy.orElse("id")))
                .stream().filter(user -> user.isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> update(Long id, User newUser, Principal user) {
        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<User> principalUser = userRepository.findByUsername(user.getName());
        if (principalUser.get().getUserRole().equals(Role.ADMIN)
                || principalUser.get().getId().equals(id)) {
            newUser.setId(id);
            newUser.setPassword(bCryptPasswordEncoder.encode(userById.get().getPassword()));
            userRepository.save(newUser);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    public ResponseEntity<HttpStatus> delete(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userById.get().setUserDeleted(Boolean.TRUE);
        userRepository.save(userById.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<User> getDeletedUser(Long id) {
        Optional<User> deletedUserById = Optional.of(userRepository.findByIdAndUserDeletedTrue(id));
        if (!deletedUserById.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deletedUserById.get(), HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getDeletedUsers() {
        List<User> deletedAllUsersById = userRepository.findAllByUserDeletedTrue();
        if (deletedAllUsersById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deletedAllUsersById, HttpStatus.OK);
    }
}
