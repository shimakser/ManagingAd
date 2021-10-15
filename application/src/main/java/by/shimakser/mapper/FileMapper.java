package by.shimakser.mapper;

import by.shimakser.dto.FileDto;
import by.shimakser.model.mongo.File;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileDto mapToDto(File file);

    List<FileDto> mapToListDto(List<File> files);

    File mapToEntity(FileDto fileDto);
}
