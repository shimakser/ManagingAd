package by.shimakser.service.ad;

import by.shimakser.keycloak.service.SecurityService;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceContainerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder bCryptPasswordEncoder;
    @Mock
    private SecurityService securityService;

    @InjectMocks
    private UserService userService;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14")
                    .withDatabaseName("${spring.datasource.db}")
                    .withUsername("${spring.datasource.username}")
                    .withPassword("${spring.datasource.password}");

    private static User USER;
    private static Long USER_ID;

    @BeforeEach
    void init() {
        USER = new User(1L, "user", "mail", "pw", Role.USER, Boolean.FALSE);
        USER_ID = USER.getId();
    }

    @Order(1)
    @Test
    void isRunning() {
        assertTrue(postgreSQLContainer.isRunning());

        assertNotNull(userService);
        assertNotNull(userRepository);
    }

    /**
     * {@link UserService#add(User)}
     */
    @Test
    void Given_CheckIsEmailBound_When_CreateUser_Then_CheckIsCorrectlyCreatedUser() {
        // given
        given(userRepository.existsUserByUserEmail(USER.getUserEmail())).willReturn(false);

        // when
        User addedUser = null;
        try {
            addedUser = userService.add(USER);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        }

        // then
        then(userRepository)
                .should()
                .save(USER);
        then(userRepository)
                .should()
                .existsUserByUserEmail(USER.getUserEmail());
        assertEquals(USER, addedUser);
    }

    /**
     * {@link UserService#add(User)}
     */
    @Test
    void Given_CheckIsEmailBound_When_CreateUser_Then_CatchExceptionByAlreadyBoundEmail() {
        // given
        given(userRepository.existsUserByUserEmail(USER.getUserEmail())).willThrow(new EntityExistsException());

        // when
        BDDCatchException.when(() -> userService.add(USER));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityExistsException.class);
        then(userRepository)
                .should(never())
                .save(USER);
    }

    /**
     * {@link UserService#get(Long)}
     */
    @Test
    void Given_SearchUserById_When_GetUser_Then_CheckIsCorrectlySearchedUser() {
        // given
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));

        // when
        User user = userService.get(USER_ID);

        // then
        then(userRepository)
                .should()
                .findById(USER_ID);
        assertEquals(USER, user);
    }

    /**
     * {@link UserService#get(Long)}
     */
    @Test
    void Given_SearchUserById_When_GetUser_Then_CatchExceptionByNotExistUser() {
        // given
        given(userRepository.findById(USER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> userService.get(USER_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * {@link UserService#getByEmail(String)}
     */
    @Test
    void Given_SearchUserByEmail_When_GetUser_Then_CheckIsCorrectlySearchedUser() {
        // given
        String testEmail = "mail";
        given(userRepository.findByUserEmail(testEmail)).willReturn(Optional.of(USER));

        // when
        User user = userService.getByEmail(testEmail);

        // then
        then(userRepository)
                .should()
                .findByUserEmail(testEmail);
        assertEquals(USER.getUserEmail(), testEmail);
        assertEquals(USER, user);
    }

    /**
     * {@link UserService#getAll(Optional, Optional, Optional)}
     */
    @Test
    void Given_SearchAllUsers_When_GetUsers_Then_CheckIsCorrectlySearchedUsers() {
        // given
        given(userRepository.findAllByUserDeletedFalse(PageRequest.of(0, 1, Sort.Direction.ASC, "id")))
                .willReturn(List.of(USER));

        // when
        List<User> users = userService.getAll(Optional.of(0), Optional.of(1), Optional.of("id"));

        // then
        then(userRepository)
                .should()
                .findAllByUserDeletedFalse(PageRequest.of(0, 1, Sort.Direction.ASC, "id"));
        assertEquals(users, List.of(USER));
    }

    /**
     * {@link UserService#update(Long, User)}
     */
    @Test
    void Given_SearchUserById_When_UpdateUser_Then_CheckIsCorrectlyUpdatedUser() {
        // given
        User testUser = new User(1L, "test", "test", "test", Role.USER, false);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
        given(securityService.checkPrincipalAccess(USER.getUsername())).willReturn(true);

        // when
        User updatedUser = null;
        try {
            updatedUser = userService.update(USER_ID, testUser);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        // then
        then(userRepository)
                .should()
                .findById(USER_ID);
        then(userRepository)
                .should()
                .save(testUser);
        assertEquals(updatedUser.getUserEmail(), testUser.getUserEmail());
    }

    /**
     * {@link UserService#update(Long, User)}
     */
    @Test
    void Given_SearchUserById_When_UpdateUser_Then_CatchExceptionByNotExistUser() {
        // given
        given(userRepository.findById(USER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> userService.update(USER_ID, USER));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
        then(userRepository)
                .should(never())
                .save(USER);
    }

    /**
     * {@link UserService#delete(Long)}
     */
    @Test
    void Given_SearchUserById_When_DeleteUser_Then_CheckIsCorrectlyDeletedUser() {
        // given
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));

        // when
        userService.delete(USER_ID);

        // then
        then(userRepository)
                .should()
                .save(USER);
        assertEquals(Boolean.TRUE, USER.isUserDeleted());
    }

    /**
     * {@link UserService#delete(Long)}
     */
    @Test
    void Given_SearchUserById_When_DeleteUser_Then_CatchExceptionByNotExistUser() {
        // given
        given(userRepository.findById(USER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> userService.delete(USER_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
        then(userRepository)
                .should(never())
                .save(USER);
        assertEquals(Boolean.FALSE, USER.isUserDeleted());
    }

    /**
     * {@link UserService#getDeletedUser(Long)}
     */
    @Test
    void Given_SearchDeletedUserById_When_GetDeletedUser_Then_CheckIsCorrectlySearchedUser() {
        // given
        given(userRepository.findByIdAndUserDeletedTrue(USER_ID)).willReturn(Optional.of(USER));

        // when
        User deletedUser = userService.getDeletedUser(USER_ID);

        // then
        then(userRepository)
                .should()
                .findByIdAndUserDeletedTrue(USER_ID);
        assertEquals(USER, deletedUser);
    }

    /**
     * {@link UserService#getDeletedUser(Long)}
     */
    @Test
    void Given_SearchDeletedUserByIncorrectId_When_GetDeletedUser_Then_CatchExceptionByNotExistUser() {
        // given
        given(userRepository.findByIdAndUserDeletedTrue(USER_ID)).willThrow(new EntityNotFoundException());

        // when
        BDDCatchException.when(() -> userService.getDeletedUser(USER_ID));

        // then
        BDDAssertions.then(caughtException()).isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * {@link UserService#getDeletedUsers()}
     */
    @Test
    void Given_SearchAllDeletedUsers_When_GetDeletedUsers_Then_CheckIsCorrectlySearchedUsers() {
        // given
        given(userRepository.findAllByUserDeletedTrue()).willReturn(List.of(USER));

        // when
        List<User> deletedUsers = userService.getDeletedUsers();

        // then
        then(userRepository)
                .should()
                .findAllByUserDeletedTrue();
        assertEquals(List.of(USER), deletedUsers);
    }

}
