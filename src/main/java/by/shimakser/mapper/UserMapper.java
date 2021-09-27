package by.shimakser.mapper;

import by.shimakser.dto.UserDto;
import by.shimakser.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "userEmail", source = "user.userEmail")
    @Mapping(target = "password", source = "user.password")
    UserDto mapToDto(User user);


    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "username", source = "userDto.username")
    @Mapping(target = "userEmail", source = "userDto.userEmail")
    @Mapping(target = "password", source = "userDto.password")
    User mapToEntity(UserDto userDto);
}
