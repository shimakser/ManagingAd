package by.shimakser.controller.ad;

import by.shimakser.dto.UserDto;
import by.shimakser.kafka.service.ProducerService;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.ad.User;
import by.shimakser.service.ad.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.rmi.AlreadyBoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws AlreadyBoundException {
        User newUser = userMapper.mapToEntity(userDto);
        producerService.send(userDto);
        userService.add(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.mapToDto(userService.get(id));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        return userMapper.mapToListDto(userService.getAll(page, size, sortBy));
    }

    @PutMapping(value = "/{id}")
    public UserDto updateUserById(@PathVariable("id") Long id,
                                  @RequestBody UserDto newUserDto,
                                  Principal principal) throws AuthenticationException {
        User user = userMapper.mapToEntity(newUserDto);
        userService.update(id, user, principal);
        return newUserDto;
    }

    @PreAuthorize("hasAuthority('user:write')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value = "/deleted/{id}")
    public UserDto getDeletedUserById(@PathVariable Long id)  {
        return userMapper.mapToDto(userService.getDeletedUser(id));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping(value = "/deleted")
    public List<UserDto> getAllDeletedUsers() {
        return userMapper.mapToListDto(userService.getDeletedUsers());
    }
}
