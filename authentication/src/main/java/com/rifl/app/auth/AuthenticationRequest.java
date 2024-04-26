package com.rifl.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @NotEmpty(message = "identifier is mandatory")
    @NotNull(message = "identifier is mandatory")
    private String identifier;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

    @JsonCreator
    public AuthenticationRequest(@JsonProperty("identifier")  String identifier,
                                 @JsonProperty("password") String password) {
        this.identifier = identifier;
        this.password = password;
    }
}
