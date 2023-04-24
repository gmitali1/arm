package com.arm.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class EcommerceUser {
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Product name is required.")
    private String username;

    @NotNull(message = "Product name is required.")
    private String password;

    public EcommerceUser(Long id,
                         @NotNull(message = "Username is required.") String username,
                         @NotNull(message = "Password is required.")String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }


    public EcommerceUser() {

    }
}
