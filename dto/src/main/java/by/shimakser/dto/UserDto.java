package by.shimakser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String userEmail;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
