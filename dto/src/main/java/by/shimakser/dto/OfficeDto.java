package by.shimakser.dto;

import lombok.Data;

import java.util.List;

@Data
public class OfficeDto {

    private Long id;
    private String officeTitle;
    private String officeAddress;
    private Double officePrice;
    private List<ContactDto> officeContacts;
    private String officeDescription;
}
