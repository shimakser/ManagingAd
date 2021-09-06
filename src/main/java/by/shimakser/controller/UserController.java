package by.shimakser.controller;

import by.shimakser.model.User;
import by.shimakser.repository.UserRepository;
import by.shimakser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/user")
    public void addUser(@RequestBody User user) {
        userService.add(user);
    }

    @GetMapping(value = "/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping(value = "/users")
    public Page<User> getAllUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return userService.getAll(page, size, sortBy);
    }

    @PutMapping(value = "/user/{id}")
    public void updateUserById(@PathVariable("id") Long id, @RequestBody User user) {
        userService.update(id, user);
    }

    @DeleteMapping(value = "/user/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
