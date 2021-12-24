package by.shimakser.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HeaderField {
    String title;
    List<HeaderField> subFields = null;

    public HeaderField(String title, List<HeaderField> subFields) {
        this.title = title;
        this.subFields = subFields;
    }

    public HeaderField(String title) {
        this.title = title;
    }
}
