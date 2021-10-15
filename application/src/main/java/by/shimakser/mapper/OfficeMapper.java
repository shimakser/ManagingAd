package by.shimakser.mapper;

import by.shimakser.dto.OfficeDto;
import by.shimakser.model.office.Office;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfficeMapper {

    OfficeDto mapToDto(Office office);

    List<OfficeDto> mapToListDto(List<Office> offices);

    Office mapToEntity(OfficeDto officeDto);
}
