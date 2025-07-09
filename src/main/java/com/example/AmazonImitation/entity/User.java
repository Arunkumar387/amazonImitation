package com.example.AmazonImitation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    public String firstname;
    public String lastname;
    public Long mobileno;
    public String email;
    @Id
    public String username;
    public String password;
}
