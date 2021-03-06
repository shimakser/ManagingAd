package by.shimakser.office.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public enum FileType {
    XLS(MediaType.valueOf("application/vnd.ms-excel"), ".xls"),
    PDF(MediaType.APPLICATION_PDF,".pdf"),
    CSV(MediaType.valueOf("application/vnd.csv"), ".csv");

    private final MediaType mediaType;
    private final String fileExtension;

    private final String fileTitle = "Export_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));
}
