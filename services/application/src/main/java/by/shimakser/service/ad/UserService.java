package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.naming.AuthenticationException;
import java.rmi.AlreadyBoundException;
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
            throw new AlreadyBoundException(ExceptionText.ALREADY_BOUND.getExceptionDescription());
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
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
        return Optional.of(userById)
                .filter(not(User::isUserDeleted))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User getByEmail(String email) throws EntityNotFoundException {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<User> getAll(Optional<Integer> page,Optional<Integer> size, Optional<String> sortBy) {
        return userRepository.findAllByUserDeletedFalse(
                PageRequest.of(page.orElse(0),
                        size.orElse(10),
                        Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public User update(Long id, User newUser, JwtAuthenticationToken token) throws EntityNotFoundException, AuthenticationException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!checkPrincipalAccess(userById.getUsername(), token)) {
            throw new AuthenticationException(ExceptionText.INSUFFICIENT_RIGHTS.getExceptionDescription());
        }

        newUser.setId(id);
        newUser.setPassword(bCryptPasswordEncoder.encode(userById.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void delete(Long id) throws EntityNotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
        userById.setUserDeleted(Boolean.TRUE);
        userRepository.save(userById);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User getDeletedUser(Long id) throws EntityNotFoundException {
        return userRepository.findByIdAndUserDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<User> getDeletedUsers() {
        return userRepository.findAllByUserDeletedTrue();
    }

    private boolean checkPrincipalAccess(String username, JwtAuthenticationToken token) {
        String principalName = token.getTokenAttributes().get("preferred_username").toString();
        boolean isAdmin = token.getTokenAttributes().get("realm_access").toString().contains(Role.ADMIN.name());

        if (isAdmin || username.equals(principalName)) {
            return true;
        }

        return false;
    }
}
