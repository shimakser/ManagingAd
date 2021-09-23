package by.shimakser.service;

import by.shimakser.model.Role;
import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    User user;

    private final Long USER_ID = 1L;

    @Test
    void add() {
        User user = new User();
        userService.add(user);

        Assert.assertTrue(user.getUserRole() == Role.USER);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void get() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        List<User> findUser = userService.get(USER_ID);

        Assert.assertNotNull(findUser);
        Assert.assertEquals(Arrays.asList(user), findUser);
    }
}