package com.arm.coordinator.service;

import com.arm.coordinator.model.EcommerceUser;
import com.arm.coordinator.repository.UserRepository;
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

    @Override
    public EcommerceUser signIn(String username, String password) {
        return this.userRepository.getEcommerceUserByUsernameAndPassword(username, password);
    }

}
