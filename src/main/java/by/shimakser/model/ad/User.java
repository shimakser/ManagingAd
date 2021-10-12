package by.shimakser.model.ad;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "usr")
public class User {

    @Id
    private Long id;
    private String username;
    private String userEmail;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role userRole;
    private boolean userDeleted = Boolean.FALSE;
}
