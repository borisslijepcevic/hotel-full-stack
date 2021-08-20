package com.boris.hotelmanagementfullstack.service;

import com.boris.hotelmanagementfullstack.dao.ConfirmationTokenRepo;
import com.boris.hotelmanagementfullstack.model.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepo confirmationTokenRepo;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepo confirmationTokenRepo) {
        this.confirmationTokenRepo = confirmationTokenRepo;
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepo.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepo.findByToken(token);
    }

    @Override
    public int setConfirmedAt(String token) {
        return confirmationTokenRepo.updateConfirmedAt(token, LocalDateTime.now());
    }
}
