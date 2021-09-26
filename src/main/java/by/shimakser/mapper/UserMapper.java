package by.shimakser.mapper;

import by.shimakser.dto.UserDto;
import by.shimakser.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target="id", source="user.id"),
            @Mapping(target="username", source="user.username"),
            @Mapping(target="userEmail", source="user.userEmail"),
            @Mapping(target="password", source="user.password")
    })
    UserDto mapToDto(User user);

    @Mappings({
            @Mapping(target="id", source="userDto.id"),
            @Mapping(target="username", source="userDto.username"),
            @Mapping(target="userEmail", source="userDto.userEmail"),
            @Mapping(target="password", source="userDto.password")
    })
    User mapToEntity(UserDto userDto);
}
