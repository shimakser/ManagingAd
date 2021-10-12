package by.shimakser.dto;

import by.shimakser.model.office.Contact;
import lombok.Data;
import org.json.JSONObject;

import java.util.List;

@Data
public class OfficeDto {

    private Long id;
    private String officeTitle;
    private String officeAddress;
    private Double convertedPrice;
    private List<Contact> officeContacts;
    private JSONObject officeDescription;
}
