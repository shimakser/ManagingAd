package by.shimakser.service.ad;

import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;
import javax.naming.AuthenticationException;
import java.rmi.AlreadyBoundException;
import java.util.*;

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
    private JwtAuthenticationToken token;

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

    @Test
    void add() {
        // given
        given(userRepository.existsUserByUserEmail(USER.getUserEmail())).willReturn(false);

        // when
        User addedUser = null;
        try {
            addedUser = userService.add(USER);
        } catch (AlreadyBoundException e) {
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

    @Test
    void add_WithExistMail() {
        // given
        given(userRepository.existsUserByUserEmail(USER.getUserEmail())).willReturn(true);

        // then
        assertThrows(AlreadyBoundException.class, () -> userService.add(USER));
        then(userRepository)
                .should(never())
                .save(USER);
    }

    @Test
    void get() {
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

    @Test
    void get_WithIncorrectId() {
        // given
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> userService.get(USER_ID));
        then(userRepository)
                .should(never())
                .save(USER);
    }

    @Test
    void getByEmail() {
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

    @Test
    void getAll() {
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


    @Test
    void update() {
        // given
        User testUser = new User(1L, "test", "test", "test", Role.USER, false);
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("preferred_username", USER.getUsername());
        tokenAttributes.put("realm_access", "USER");
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
        given(token.getTokenAttributes()).willReturn(tokenAttributes);

        // when
        User updatedUser = null;
        try {
            updatedUser = userService.update(USER_ID, testUser, token);
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

    @Test
    void update_WithIncorrectId() {
        // given
        User testUser = new User(1L, "test", "test", "test", Role.USER, false);
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> userService.update(USER_ID, testUser, token));
        then(userRepository)
                .should(never())
                .save(testUser);
    }

    @Test
    void delete() {
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

    @Test
    void delete_WithIncorrectId() {
        // given
        given(userRepository.findById(USER_ID)).willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> userService.delete(USER_ID));
        then(userRepository)
                .should(never())
                .save(USER);
        assertEquals(Boolean.FALSE, USER.isUserDeleted());
    }

    @Test
    void getDeletedUser() {
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

    @Test
    void getDeletedUser_WithIncorrectId() {
        // given
        given(userRepository.findByIdAndUserDeletedTrue(USER_ID))
                .willReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> userService.getDeletedUser(USER_ID));
        then(userRepository)
                .should()
                .findByIdAndUserDeletedTrue(USER_ID);
    }

    @Test
    void getDeletedUsers() {
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
