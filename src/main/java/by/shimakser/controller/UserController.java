package by.shimakser.controller;

import by.shimakser.dto.UserDto;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.User;
import by.shimakser.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws AlreadyBoundException {
        User newUser = UserMapper.INSTANCE.mapToEntity(userDto);
        userService.add(newUser);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{id}")
    public List<UserDto> getUserById(@PathVariable Long id) throws NotFoundException {
        List<UserDto> user = userService.get(id).stream().map(UserMapper.INSTANCE::mapToDto).collect(Collectors.toList());
        return user;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users")
    public List<UserDto> getAllUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy
    ) {
        List<UserDto> users = userService.getAll(page, size, sortBy).stream().map(UserMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
        return users;
    }

    @PutMapping(value = "/users/{id}")
    public UserDto updateUserById(@PathVariable("id") Long id,
                                  @RequestBody UserDto newUserDto,
                                  Principal principal) throws NotFoundException {
        User user = UserMapper.INSTANCE.mapToEntity(newUserDto);
        userService.update(id, user, principal);
        return newUserDto;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) throws NotFoundException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/deleted/{id}")
    public UserDto getDeletedUserById(@PathVariable Long id) throws NotFoundException {
        UserDto userDto = UserMapper.INSTANCE.mapToDto(userService.getDeletedUser(id));
        return userDto;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/deleted")
    public List<UserDto> getAllDeletedUsers() {
        List<UserDto> usersDto = userService.getDeletedUsers().stream().map(UserMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
        return usersDto;
    }
}
