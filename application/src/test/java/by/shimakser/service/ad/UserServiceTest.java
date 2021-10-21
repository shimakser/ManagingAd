package by.shimakser.service.ad;

import by.shimakser.MongoDbContainer;
import by.shimakser.PostgresDBContainer;
import by.shimakser.model.ad.Role;
import by.shimakser.model.ad.User;
import by.shimakser.repository.ad.UserRepository;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @ClassRule
    public static PostgreSQLContainer<PostgresDBContainer> postgreSQLContainer
            = PostgresDBContainer.getInstance();

    @ClassRule
    public static MongoDbContainer mongoDbContainer = new MongoDbContainer();

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void add() {
        User newUser = new User(4L, "newUser", "newuser@gmail.com", "newuser",
                Role.USER, Boolean.FALSE);
        userRepository.save(newUser);

        Assert.assertNotNull(userRepository.findById(4L));
        assertThat(userRepository.findById(4L)).contains(newUser);
        Assert.assertEquals(userRepository.findById(4L).get().getUserEmail(), "newuser@gmail.com");
    }

    @Test
    @Transactional
    public void get() {
        Assert.assertNotNull(userRepository.findById(1L));
        Assert.assertEquals(userRepository.findById(1L).get().getUsername(), "admin");
    }

    @Test
    @Transactional
    public void getAllWithFindingUser() {
        Assert.assertEquals(userRepository.findAllByUserDeletedFalse(PageRequest
                .of(0, 2, Sort.by("id"))).size(), 2);
    }

    @Test
    @Transactional
    public void getAllWithEmptyList() {
        Assert.assertTrue(userRepository.findAllByUserDeletedFalse(PageRequest
                .of(1, 2, Sort.by("id"))).isEmpty());
    }
}
