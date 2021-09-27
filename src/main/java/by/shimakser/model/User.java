package by.shimakser.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }
}
