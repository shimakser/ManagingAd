package by.shimakser.mapper;

import by.shimakser.dto.UserDto;
import by.shimakser.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto mapToDto(User user);
    User mapToEntity(UserDto userDto);
}
