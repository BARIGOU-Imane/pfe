package com.rifl.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rifl.app.role.Role;
import com.rifl.app.role.UserRole;
import com.rifl.app.service.Service;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstName;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastName;
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
    @NotEmpty(message = "identifier is mandatory")
    @NotNull(message = "identifier is mandatory")
    private String identifier;
    private String matricule;
    private String service;
    private String cin;
    private String firstPhoneNumber;
    private String lastPhoneNumber;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private LocalDate dateOfBirth;
    private int status;
    private boolean accountLocked;
    private boolean enabled;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    private List<String> roles;

    @JsonCreator
    public RegistrationRequest(@JsonProperty("firstName") String firstName,
                               @JsonProperty("lastName") String lastName,
                               @JsonProperty("email") String email,
                               @JsonProperty("password")String password,
                               @JsonProperty("matricule")String matricule,
                               @JsonProperty("identifier")String identifier,
                               @JsonProperty("service") String service,
                               @JsonProperty("cin")String cin,
                               @JsonProperty("firstPhoneNumber")String firstPhoneNumber,
                               @JsonProperty("lastPhoneNumber")String lastPhoneNumber,
                               @JsonProperty("contractStartDate")LocalDate contractStartDate,
                               @JsonProperty("contractEndDate")LocalDate contractEndDate,
                               @JsonProperty("dateOfBirth")LocalDate dateOfBirth,
                               @JsonProperty("status")int status,
                               @JsonProperty("accountLocked")boolean accountLocked,
                               @JsonProperty("enabled")boolean enabled,
                               @JsonProperty("createdDate")LocalDateTime createdDate,
                               @JsonProperty("lastModifiedDate")LocalDateTime lastModifiedDate,
                               @JsonProperty("roles") List<String> roles
) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.matricule = matricule;
        this.identifier = identifier;
        this.service = service;
        this.cin = cin;
        this.firstPhoneNumber = firstPhoneNumber;
        this.lastPhoneNumber = lastPhoneNumber;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.dateOfBirth = dateOfBirth;
        this.status = status;//
        this.accountLocked = accountLocked;//
        this.enabled = enabled;//
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.roles = roles;
    }

}