package by.shimakser.controller;

import by.shimakser.model.User;
import by.shimakser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<User> getAllUsers() {
        return userService.getAll();
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
