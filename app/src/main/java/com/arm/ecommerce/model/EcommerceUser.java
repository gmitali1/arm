package com.arm.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

/**
 * Represents an Ecommerce user in the system.
 */
@Entity
public class EcommerceUser {

    /**
     * The ID of the Ecommerce user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * The username of the Ecommerce user.
     */
    @NotNull(message = "Product name is required.")
    private String username;
    /**
     * The password of the Ecommerce user.
     */
    @NotNull(message = "Product name is required.")
    private String password;

    /**
     * Constructs a new EcommerceUser with the specified ID, username, and password.
     *
     * @param id       The ID of the EcommerceUser.
     * @param username The username of the EcommerceUser.
     * @param password The password of the EcommerceUser.
     */
    public EcommerceUser(Long id,
                         @NotNull(message = "Username is required.") String username,
                         @NotNull(message = "Password is required.") String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a new, empty EcommerceUser.
     */
    public EcommerceUser() {

    }

    /**
     * Returns the ID of the Ecommerce user.
     *
     * @return The ID of the Ecommerce user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the username of the Ecommerce user.
     *
     * @return The username of the Ecommerce user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the Ecommerce user.
     *
     * @return The password of the Ecommerce user.
     */
    public String getPassword() {
        return password;
    }
}
