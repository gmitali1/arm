package com.arm.ecommerce.service;

import com.arm.ecommerce.model.EcommerceUser;
import com.arm.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<EcommerceUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public EcommerceUser signup(EcommerceUser ecommerceUser) {
        return this.userRepository.save(ecommerceUser);
    }
}
