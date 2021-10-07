package by.shimakser.service;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

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
        boolean isUserByEmailExist = userRepository.existsUserByUserEmail(user.getUserEmail());

        if (isUserByEmailExist) {
            throw new AlreadyBoundException(ExceptionText.AlreadyBound.getExceptionText());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(Role.USER);
        user.setUserDeleted(Boolean.FALSE);
        userRepository.save(user);
        return user;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User get(Long id) throws EntityNotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
        return Optional.of(userById)
                .filter(not(User::isUserDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User getByEmail(String email) throws EntityNotFoundException {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional
    public List<User> getAll(Optional<Integer> page,
                             Optional<Integer> size,
                             Optional<String> sortBy
    ) {
        return userRepository.findAllByUserDeletedFalse(
                PageRequest.of(page.orElse(0),
                        size.orElse(10),
                        Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class, AuthorizationServiceException.class})
    public User update(Long id, User newUser, Principal user)
            throws EntityNotFoundException,AuthenticationException, AuthorizationServiceException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));

        User principalUser = userRepository.findByUsername(user.getName())
                .orElseThrow(() -> new AuthorizationServiceException(ExceptionText.AuthorizationService.getExceptionText()));
        if (!principalUser.getUserRole().equals(Role.ADMIN)
                || principalUser.getId().equals(id)) {
            throw new AuthenticationException(ExceptionText.Authentication.getExceptionText());
        }
        newUser.setId(id);
        newUser.setPassword(bCryptPasswordEncoder.encode(userById.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void delete(Long id) throws EntityNotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
        userById.setUserDeleted(Boolean.TRUE);
        userRepository.save(userById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User getDeletedUser(Long id) throws EntityNotFoundException {
        return userRepository.findByIdAndUserDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.EntityNotFound.getExceptionText()));
    }

    @Transactional
    public List<User> getDeletedUsers() {
        return userRepository.findAllByUserDeletedTrue();
    }
}
