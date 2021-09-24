package by.shimakser.mapper.impl;

import by.shimakser.dto.UserDto;
import by.shimakser.mapper.UserMapper;
import by.shimakser.model.User;
import org.springframework.stereotype.Component;

@Component("userMapperImpl")
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setUserEmail(user.getUserEmail());
        userDto.setPassword(user.getPassword());
        
        return userDto;
    }

    @Override
    public User mapToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setUserEmail(userDto.getUserEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
