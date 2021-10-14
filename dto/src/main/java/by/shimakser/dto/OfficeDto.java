package by.shimakser.dto;

import lombok.Data;
import org.json.JSONObject;

import java.util.List;

@Data
public class OfficeDto {

    private Long id;
    private String officeTitle;
    private String officeAddress;
    private Double officePrice;
    private List officeContacts;
    private JSONObject officeDescription;
}