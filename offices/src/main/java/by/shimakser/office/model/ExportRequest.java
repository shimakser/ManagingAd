package by.shimakser.office.model;

import by.shimakser.dto.EntityType;
import lombok.Data;

@Data
public class ExportRequest {

    private String pathToFile;
    private FileType fileType;
    private EntityType entityType;
}
