package com.maveric.tsp.registerservice.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordGenerator {

    @Value("${tsp.password.length}")
    private int passwordLength;

    @Value("${tsp.password.pattern}")
    private String passwordRegex;

    @Value("${tsp.password.charecterset}")
    private String characterSet;

    public  String generatePassword() {
        return generateSecureRandomPassword(passwordLength, passwordRegex);
    }

    private String generateSecureRandomPassword(int length, String regexPattern) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        while (!passwordMatchesPattern(password.toString(), regexPattern)) {
            password.setLength(0); // Clear the StringBuilder
            for (int i = 0; i < length; i++) {
                char randomChar = getRandomChar(secureRandom);
                password.append(randomChar);
            }
        }
        return password.toString();
    }

    private char getRandomChar(SecureRandom secureRandom) {
        int index = secureRandom.nextInt(characterSet.length());
        return characterSet.charAt(index);
    }

    private boolean passwordMatchesPattern(String password, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
