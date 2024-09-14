package com.springboot.backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id // user_UUID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private String user_password;

    public Long getId() {
        return _id;
    }
    public String getPassword() {
        return user_password;
    }

}
