package by.shimakser.service.ad;

import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceContainerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14")
                    .withDatabaseName("${spring.datasource.db}")
                    .withUsername("${spring.datasource.username}")
                    .withPassword("${spring.datasource.password}");

    private static final User USER = new User(4L, "user", "mail", "pw", Role.USER, Boolean.FALSE);
    private static final Long USER_ID = USER.getId();

    @Order(1)
    @Test
    void isRunning() {
        assertTrue(postgreSQLContainer.isRunning());

        assertNotNull(userService);
        assertNotNull(userRepository);
    }

    @Test
    void add() throws AlreadyBoundException {
        given(userRepository.existsUserByUserEmail(USER.getUserEmail()))
                .willReturn(false);
        given(userRepository.save(USER))
                .willReturn(USER);

        User addedUser = userService.add(USER);

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
        given(userRepository.existsUserByUserEmail(USER.getUserEmail()))
                .willReturn(true);

        try {
            userService.add(USER);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        assertThrows(AlreadyBoundException.class, () -> userService.add(USER));
        then(userRepository)
                .should(never())
                .save(USER);
    }

    @Test
    void get() {
        given(userRepository.findById(USER_ID))
                .willReturn(Optional.of(USER));

        User user = userService.get(USER_ID);

        then(userRepository)
                .should()
                .findById(USER_ID);
        assertEquals(USER, user);
    }

    @Test
    void get_WithIncorrectId() {
        Long incorrectId = 5L;

        given(userRepository.findById(incorrectId))
                .willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.get(incorrectId));
        then(userRepository)
                .should(never())
                .save(USER);
    }

    @Test
    void delete() {
        given(userRepository.findById(USER_ID))
                .willReturn(Optional.of(USER));

        userService.delete(USER_ID);

        then(userRepository)
                .should()
                .save(USER);
        assertEquals(Boolean.TRUE, USER.isUserDeleted());
    }

    @Test
    void getDeletedUser() {
        given(userRepository.findByIdAndUserDeletedTrue(USER_ID))
                .willReturn(Optional.of(USER));

        User deletedUser = userService.getDeletedUser(USER_ID);

        then(userRepository)
                .should()
                .findByIdAndUserDeletedTrue(USER_ID);
        assertEquals(USER, deletedUser);
    }
}
