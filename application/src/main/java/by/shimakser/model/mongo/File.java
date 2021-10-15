package by.shimakser.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "File")
public class File {

    @Id
    private Long id;

    @Field(value = "created_date")
    private LocalDateTime fileCreatedDate;

    @Field(value = "content")
    private String content;

    @Field(value = "file_deleted")
    private boolean fileDeleted = Boolean.FALSE;
}
