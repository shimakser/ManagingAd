package by.shimakser.mapper;

import by.shimakser.dto.UserDto;
import by.shimakser.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToDto(User user);

    List<UserDto> mapToListDto(List<User> users);

    User mapToEntity(UserDto userDto);
}
