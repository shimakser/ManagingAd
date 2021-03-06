package by.shimakser.controller.ad;

import by.shimakser.dto.UserDto;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.ad.User;
import by.shimakser.service.ad.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws EntityExistsException {
        User newUser = userMapper.mapToEntity(userDto);
        User addedUser = userService.add(newUser);
        return new ResponseEntity<>(userMapper.mapToDto(addedUser), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.mapToDto(userService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> size,
                                     @RequestParam Optional<String> sortBy) {
        return userMapper.mapToListDto(userService.getAll(page, size, sortBy));
    }

    @PutMapping(value = "/{id}")
    public UserDto updateUserById(@PathVariable("id") Long id, @RequestBody UserDto newUserDto) throws AuthenticationException {
        User user = userMapper.mapToEntity(newUserDto);
        User updatedUser = userService.update(id, user);
        return userMapper.mapToDto(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deleted/{id}")
    public UserDto getDeletedUserById(@PathVariable Long id) {
        return userMapper.mapToDto(userService.getDeletedUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/deleted")
    public List<UserDto> getAllDeletedUsers() {
        return userMapper.mapToListDto(userService.getDeletedUsers());
    }
}
