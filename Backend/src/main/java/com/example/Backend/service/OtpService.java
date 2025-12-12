package com.example.Backend.service;

import java.util.Map;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

@Service
public class OtpService {

    // email -> OTP
    private Map<String, String> otpStorage = new HashMap<>();

    // email -> expiration time
    private Map<String, LocalDateTime> otpExpiration = new HashMap<>();

    // Générer un OTP de 6 chiffres
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        otpExpiration.put(email, LocalDateTime.now().plusMinutes(5)); // expire dans 5 minutes
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;

        String storedOtp = otpStorage.get(email);
        LocalDateTime expiredAt = otpExpiration.get(email);

        if (LocalDateTime.now().isAfter(expiredAt)) {
            otpStorage.remove(email);
            otpExpiration.remove(email);
            return false;
        }

        boolean isValid = storedOtp.equals(otp);

        if (isValid) {
            otpStorage.remove(email);
            otpExpiration.remove(email);
        }

        return isValid;
    }
}

