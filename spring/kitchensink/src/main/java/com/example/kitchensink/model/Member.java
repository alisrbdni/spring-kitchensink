package com.example.kitchensink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Document(collection = "members")
public class Member {

    @Id
    private String id; // Using a String for MongoDB _id.

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 25, message = "Name must be between 1 and 25 characters")
    @Pattern(regexp = "[^0-9]*", message = "Name must not contain numbers")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 digits")
    @Digits(integer = 12, fraction = 0, message = "Phone number must contain only digits")
    private String phoneNumber;

    public Member() {
        // default constructor
    }

    public Member(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // getters/setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}