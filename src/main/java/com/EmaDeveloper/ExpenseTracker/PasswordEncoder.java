package com.EmaDeveloper.ExpenseTracker;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Raw Password: " + encodedPassword);

    }
}
