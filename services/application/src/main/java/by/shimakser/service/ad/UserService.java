package by.shimakser.service.ad;

import by.shimakser.exception.ExceptionText;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import by.shimakser.security.service.SecurityService;
import by.shimakser.service.kafka.ProducerService;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final SecurityService securityService;
    private final TransactionOutbox outbox;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final String DEFAULT_FIELD_SORT = "id";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder, TransactionOutbox outbox,
                       SecurityService securityService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityService = securityService;
        this.outbox = outbox;
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
        outbox.schedule(ProducerService.class).sendUser(user);

        log.info("User {} successfully registered with email {}.", user.getId(), user.getUserEmail());
        return user;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User get(Long id) throws EntityNotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));

        log.info("Search user with id {}.", id);
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
    public List<User> getAll(Optional<Integer> page, Optional<Integer> size, Optional<String> sortBy) {
        log.info("Searched all users by page {}, size {}, sort by {}.",
                page.orElseGet(() -> DEFAULT_PAGE), size.orElseGet(() -> DEFAULT_PAGE_SIZE), sortBy.orElseGet(() -> DEFAULT_FIELD_SORT));
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

        log.info("User {} updated.", id);
        return newUser;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void delete(Long id) throws EntityNotFoundException {
        User userById = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
        userById.setUserDeleted(Boolean.TRUE);
        userRepository.save(userById);

        log.info("User {} deleted.", id);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public User getDeletedUser(Long id) throws EntityNotFoundException {
        log.info("Search deleted user {}.", id);
        return userRepository.findByIdAndUserDeletedTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionText.ENTITY_NOT_FOUND.getExceptionDescription()));
    }

    @Transactional
    public List<User> getDeletedUsers() {
        log.info("Search all deleted users.");
        return userRepository.findAllByUserDeletedTrue();
    }
}
