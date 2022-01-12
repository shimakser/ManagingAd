package by.shimakser.office.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExportRequest {

    private String pathToFile;
    private FileType fileType;
    private EntityType entityType;

    public ExportRequest(FileType fileType, EntityType entityType) {
        this.fileType = fileType;
        this.entityType = entityType;
    }
}
