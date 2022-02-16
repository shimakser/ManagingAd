package by.shimakser.controller.ad;

import by.shimakser.dto.UserDto;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.ad.User;
import by.shimakser.service.ad.UserService;
import by.shimakser.service.kafka.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final ProducerService producerService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, ProducerService producerService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws EntityExistsException {
        User newUser = userMapper.mapToEntity(userDto);
        producerService.sendUser(userDto);
        userService.add(newUser);
        log.info("Added user: " + userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Search user with id: " + id);
        return userMapper.mapToDto(userService.get(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> size,
                                     @RequestParam Optional<String> sortBy
    ) {
        log.info("Search all users");
        return userMapper.mapToListDto(userService.getAll(page, size, sortBy));
    }

    @PutMapping(value = "/{id}")
    public UserDto updateUserById(@PathVariable("id") Long id, @RequestBody UserDto newUserDto) throws AuthenticationException {
        User user = userMapper.mapToEntity(newUserDto);
        log.info("Updated user with id: " + id);
        return userMapper.mapToDto(userService.update(id, user));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        userService.delete(id);
        log.info("Deleted user with id: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/deleted/{id}")
    public UserDto getDeletedUserById(@PathVariable Long id) {
        return userMapper.mapToDto(userService.getDeletedUser(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/deleted")
    public List<UserDto> getAllDeletedUsers() {
        return userMapper.mapToListDto(userService.getDeletedUsers());
    }
}
