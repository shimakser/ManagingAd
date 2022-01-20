package by.shimakser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranslationRequest {

    private String entityName;
    private String language;
}
