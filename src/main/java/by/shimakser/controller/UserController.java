package by.shimakser.controller;

import by.shimakser.dto.UserDto;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.User;
import by.shimakser.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.rmi.AlreadyBoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, @Qualifier("userMapperImpl") UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws AlreadyBoundException {
        User newUser = userMapper.mapToEntity(userDto);
        userService.add(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<List<UserDto>> getUserById(@PathVariable Long id) throws NotFoundException {
        List<UserDto> user = userService.get(id).stream().map(userMapper::mapToDto).collect(Collectors.toList());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        List<UserDto> users = userService.getAll(page, size, sortBy).stream().map(userMapper::mapToDto)
                .collect(Collectors.toList());
        if (users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable("id") Long id,
                                               @RequestBody UserDto newUserDto,
                                               Principal principal) throws NotFoundException {
        User user = userMapper.mapToEntity(newUserDto);
        userService.update(id, user, principal);
        return new ResponseEntity<>(newUserDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) throws NotFoundException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/deleted/{id}")
    public ResponseEntity<UserDto> getDeletedUserById(@PathVariable Long id) throws NotFoundException {
        UserDto userDto = userMapper.mapToDto(userService.getDeletedUser(id));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/deleted")
    public ResponseEntity<List<UserDto>> getAllDeletedUsers() {
        List<UserDto> usersDto = userService.getDeletedUsers().stream().map(userMapper::mapToDto)
                .collect(Collectors.toList());
        if (usersDto.isEmpty()) {
            return new ResponseEntity<>(usersDto, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
}
