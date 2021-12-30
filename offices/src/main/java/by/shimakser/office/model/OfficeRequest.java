package by.shimakser.office.model;

import lombok.Data;

@Data
public class OfficeRequest {

    private Long id;
    private String pathToFile;
    private FileType fileType;
}
