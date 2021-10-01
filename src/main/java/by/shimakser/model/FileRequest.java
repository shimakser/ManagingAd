package by.shimakser.model;

import lombok.Data;

@Data
public class FileRequest {

    private Long id;
    private String pathToFile;

    public FileRequest() {
    }

    public FileRequest(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
