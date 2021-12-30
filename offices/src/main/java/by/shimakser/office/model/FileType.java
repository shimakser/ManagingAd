package by.shimakser.office.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@AllArgsConstructor
public enum FileType {
    XLS(MediaType.valueOf("application/vnd.ms-excel"), ".xls"),
    PDF(MediaType.APPLICATION_PDF, ".pdf"),
    HTML(MediaType.APPLICATION_PDF, ".pdf");

    private final MediaType mediaType;
    private final String fileExtension;
}
