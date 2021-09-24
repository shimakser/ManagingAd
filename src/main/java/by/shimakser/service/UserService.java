package by.shimakser.service;

import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import javassist.NotFoundException;
import netscape.security.ForbiddenTargetException;
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
    public User add(User user) throws AlreadyBoundException {
        Optional<User> userFromDBByEmail = userRepository.findByUserEmail(user.getUserEmail());

        if (userFromDBByEmail.isPresent()) {
            throw new AlreadyBoundException("Entered mail is already taken.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(Role.USER);
        user.setUserDeleted(Boolean.FALSE);
        userRepository.save(user);
        return user;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public List<User> get(Long id) throws NotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found."));
        List<User> user = Stream.of(userById).filter(u -> u.isUserDeleted() == Boolean.FALSE).collect(Collectors.toList());
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

    @Transactional(rollbackFor = {NotFoundException.class, ForbiddenTargetException.class})
    public User update(Long id, User newUser, Principal user) throws NotFoundException, ForbiddenTargetException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found."));

        Optional<User> principalUser = userRepository.findByUsername(user.getName());
        if (!principalUser.get().getUserRole().equals(Role.ADMIN)
                || principalUser.get().getId().equals(id)) {
            throw new ForbiddenTargetException("Insufficient rights to edit the user.");
        }
        newUser.setId(id);
        newUser.setPassword(bCryptPasswordEncoder.encode(userById.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id) throws NotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found."));
        userById.setUserDeleted(Boolean.TRUE);
        userRepository.save(userById);
    }

    @Transactional(rollbackFor = NotFoundException.class)
    public User getDeletedUser(Long id) throws NotFoundException {
        User deletedUserById = userRepository.findByIdAndUserDeletedTrue(id)
                .orElseThrow(() -> new NotFoundException("Deleted user is not found."));
        return deletedUserById;
    }

    @Transactional
    public List<User> getDeletedUsers() {
        List<User> deletedAllUsersById = userRepository.findAllByUserDeletedTrue();
        return deletedAllUsersById;
    }
}
