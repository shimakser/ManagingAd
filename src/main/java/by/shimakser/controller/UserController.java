package by.shimakser.controller;

import by.shimakser.model.User;
import by.shimakser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/user")
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<User>> getUserById(@PathVariable Long id) {
        return userService.get(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return userService.getAll(page, size, sortBy);
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<HttpStatus> updateUserById(@PathVariable("id") Long id, @RequestBody User newUser, Principal user) {
        return userService.update(id, newUser, user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/user/deleted/{id}")
    public ResponseEntity<User> getDeletedUserById(@PathVariable Long id) {
        return userService.getDeletedUser(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/deleted")
    public ResponseEntity<List<User>> getAllDeletedUsers() {
        return userService.getDeletedUsers();
    }
}
