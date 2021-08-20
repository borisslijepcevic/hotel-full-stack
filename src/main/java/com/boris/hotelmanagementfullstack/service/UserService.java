package com.boris.hotelmanagementfullstack.service;

import com.boris.hotelmanagementfullstack.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findUserById(Long id);
    User addUser(User user);
    User updateUser(User user);
    void deleteUserById(Long id);
    String signUpUser(User user);
    int enableUser(String email);

}
