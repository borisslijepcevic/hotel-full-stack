package com.boris.hotelmanagementfullstack.service;

import com.boris.hotelmanagementfullstack.dao.UserRepo;
import com.boris.hotelmanagementfullstack.exception.UserNotFoundException;
import com.boris.hotelmanagementfullstack.model.ConfirmationToken;
import com.boris.hotelmanagementfullstack.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final static String USERNAME_NOT_FOUND_MSG =
            "User with email %s not found.";
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findUserByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MSG, email)));
    }


    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepo.findUserById(id).
                orElseThrow(() -> new UserNotFoundException("User with id = " +id+ " not found."));
    }

    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public String signUpUser(User user) {
        boolean userExists =
                userRepo.findUserByEmail(user.getEmail())
                        .isPresent();
        if(userExists) {
            throw new RuntimeException("User already registered.");
        }

        String encodedPassword =
                bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken =
                new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;

    }

    @Override
    public int enableUser(String email) {
        return userRepo.enableUser(email);
    }
}
