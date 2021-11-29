package by.shimakser.dto;

import lombok.Data;

@Data
public class ContactDto {

    private Long id;
    private String contactPhoneNumber;
    private String contactEmail;
    private String contactSite;

    @Override
    public String toString() {
        return id + "," + contactPhoneNumber + "," + contactEmail + "," + contactSite;
    }
}
