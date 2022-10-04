package com.ninjaone.backendinterviewproject.resources.user.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "users")
public class UserModel {
    @Id
    private String username;
    private String password;
    @NotBlank
    private String name;

    public String getPassword() {
        return new String(
                Base64.getDecoder().decode(password),
                StandardCharsets.UTF_8
        );
    }
}
