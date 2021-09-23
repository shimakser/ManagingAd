package by.shimakser.service;

import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
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

    @Transactional(rollbackFor = AlreadyBoundException.class)
    public void add(User user) throws AlreadyBoundException {
        Optional<User> userFromDBByEmail = userRepository.findByUserEmail(user.getUserEmail());

        if (userFromDBByEmail.isPresent()) {
            throw new AlreadyBoundException("Entered mail is already taken.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(Role.USER);
        userRepository.save(user);
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public List<User> get(Long id) throws NotFoundException {
        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new NotFoundException("User is not found.");
        }
        List<User> user = Stream.of(userById.get()).filter(u -> u.isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
        return user;
    }

    @Transactional
    public List<User> getAll(Optional<Integer> page,
                                             Optional<Integer> size,
                                             Optional<String> sortBy
    ) {
        List<User> users = userRepository.findAll(
                        PageRequest.of(page.orElse(0),
                                size.orElse(userRepository.findAll().size()),
                                Sort.Direction.ASC, sortBy.orElse("id")))
                .stream().filter(user -> user.isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
        return users;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public void update(Long id, User newUser, Principal user) throws NotFoundException {
        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new NotFoundException("User is not found.");
        }
        Optional<User> principalUser = userRepository.findByUsername(user.getName());
        if (principalUser.get().getUserRole().equals(Role.ADMIN)
                || principalUser.get().getId().equals(id)) {
            newUser.setId(id);
            newUser.setPassword(bCryptPasswordEncoder.encode(userById.get().getPassword()));
            userRepository.save(newUser);
        }
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id) throws NotFoundException {
        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new NotFoundException("User is not found.");
        }
        userById.get().setUserDeleted(Boolean.TRUE);
        userRepository.save(userById.get());
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public User getDeletedUser(Long id) throws NotFoundException {
        Optional<User> deletedUserById = userRepository.findByIdAndUserDeletedTrue(id);
        if (!deletedUserById.isPresent()) {
            throw new NotFoundException("Deleted user is not found.");
        }
        return deletedUserById.get();
    }

    @Transactional
    public List<User> getDeletedUsers() {
        List<User> deletedAllUsersById = userRepository.findAllByUserDeletedTrue();
        return deletedAllUsersById;
    }
}
