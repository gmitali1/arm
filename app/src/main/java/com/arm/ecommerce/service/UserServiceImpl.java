package com.arm.ecommerce.service;

import com.arm.ecommerce.model.EcommerceUser;
import com.arm.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing users in the ecommerce system.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new instance of {@code UserServiceImpl} with the specified {@code UserRepository}.
     *
     * @param userRepository the {@code UserRepository} to be used by this service
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all ecommerce users.
     *
     * @return an {@code Iterable} of {@code EcommerceUser}s
     */
    @Override
    public Iterable<EcommerceUser> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Registers a new user in the system.
     *
     * @param ecommerceUser the {@code EcommerceUser} object representing the user to be registered
     * @return the registered {@code EcommerceUser} object
     */
    @Override
    public EcommerceUser signup(EcommerceUser ecommerceUser) {
        return this.userRepository.save(ecommerceUser);
    }
}
