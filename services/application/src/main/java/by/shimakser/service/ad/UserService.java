package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.keycloak.service.SecurityService;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final SecurityService securityService;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 1;
    private static final String DEFAULT_FIELD_SORT = "id";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder, SecurityService securityService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityService = securityService;
    }

    @Transactional(rollbackFor = EntityExistsException.class)
    public User add(User user) throws EntityExistsException {
        boolean isUserByEmailExist = userRepository.existsUserByUserEmail(user.getUserEmail());

        if (isUserByEmailExist) {
            throw new EntityExistsException(ExceptionText.ALREADY_BOUND.getExceptionDescription());
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
                PageRequest.of(page.orElse(DEFAULT_PAGE),
                        size.orElse(DEFAULT_PAGE_SIZE),
                        Sort.Direction.ASC, sortBy.orElse(DEFAULT_FIELD_SORT)));
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, AuthenticationException.class})
    public User update(Long id, User newUser) throws EntityNotFoundException, AuthenticationException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        if (!securityService.checkPrincipalAccess(userById.getUsername())) {
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
}
