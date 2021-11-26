package by.shimakser.dto;

import lombok.Data;

@Data
public class CurrencyDto {

    private String id;
    private String numCode;
    private String charCode;
    private String nominal;
    private String name;
    private String value;
    private String previous;
}
