package by.shimakser.office.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OfficeOperationInfo {

    private Status status;
    private String path;
}
