package by.shimakser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SslRequest {

    private String common;          // CN
    private String country;         // C
    private String state;           // S
    private String locality;        // L
    private String organization;    // O
    private String email;
}
