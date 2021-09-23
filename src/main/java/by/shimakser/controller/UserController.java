package by.shimakser.controller;

import by.shimakser.model.User;
import by.shimakser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
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
        boolean addingCheck = userService.add(user);
        if (!addingCheck) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<User>> getUserById(@PathVariable Long id) {
        List<User> user = userService.get(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        List<User> users = userService.getAll(page, size, sortBy);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<HttpStatus> updateUserById(@PathVariable("id") Long id,
                                                     @RequestBody User newUser, Principal user) throws SQLException {
        boolean updatingCheck = userService.update(id, newUser, user);
        if (!updatingCheck) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        boolean deleteCheck = userService.delete(id);
        if (!deleteCheck) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/user/deleted/{id}")
    public ResponseEntity<User> getDeletedUserById(@PathVariable Long id) {
        Optional<User> user = userService.getDeletedUser(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/deleted")
    public ResponseEntity<List<User>> getAllDeletedUsers() {
        List<User> users = userService.getDeletedUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
