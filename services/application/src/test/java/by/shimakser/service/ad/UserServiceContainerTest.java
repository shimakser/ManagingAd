package by.shimakser.service.ad;

import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceContainerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14")
                    .withDatabaseName("${spring.datasource.db}")
                    .withUsername("${spring.datasource.username}")
                    .withPassword("${spring.datasource.password}");

    private User user = new User(4L, "user", "mail", "pw", Role.USER, Boolean.FALSE);;

    @Order(1)
    @Test
    void isRunning() {
        assertTrue(postgreSQLContainer.isRunning());

        assertNotNull(userRepository);
        assertNotNull(userService);
    }

    @Order(2)
    @Test
    void add() {
        userRepository.delete(user);

        assertDoesNotThrow(() -> userService.add(user));
        assertEquals(userService.get(4L), user);
    }

    @Order(3)
    @Test
    void add_WithExistMail() {
        assertThrows(AlreadyBoundException.class, () -> userService.add(user));
    }

    @Order(4)
    @Test
    void get() {
        assertDoesNotThrow(() -> userService.get(4L));
        assertEquals(userService.get(4L).getUserEmail(), user.getUserEmail());
    }

    @Order(5)
    @Test
    void get_WithIncorrectId() {
        assertThrows(EntityNotFoundException.class, () -> userService.get(5L));
    }

    @Order(6)
    @Test
    void delete() {
        assertDoesNotThrow(() -> userService.delete(4L));

        userService.delete(4L);
        assertEquals(userRepository.findById(4L).get().isUserDeleted(), Boolean.TRUE);
    }

    @Order(7)
    @Test
    void getDeletedUser() {
        assertDoesNotThrow(() -> userService.getDeletedUser(4L));

        assertEquals(userService.getDeletedUser(4L).isUserDeleted(), Boolean.TRUE);
        userRepository.delete(user);
    }
}
