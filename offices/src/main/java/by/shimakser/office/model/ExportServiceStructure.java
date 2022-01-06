package by.shimakser.office.model;

import by.shimakser.dto.EntityType;
import by.shimakser.office.service.ExportService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExportServiceStructure {
    private FileType fileType;
    private Map<EntityType, ExportService<?>> serviceMap;
}
