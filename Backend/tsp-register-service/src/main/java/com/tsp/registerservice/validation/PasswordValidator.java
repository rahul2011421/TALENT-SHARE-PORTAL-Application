package com.tsp.registerservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.regex.Pattern;

@Component
@Slf4j
public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9].*[0-9])(?=.*[!@#$%^&*()-+=])(?=\\S+$).{8,15}$";
    @Override

    public boolean isValid(String encodedPassword, ConstraintValidatorContext context) {
        log.info("PASSWORD IN VALIDATOR:"+encodedPassword);
        String password=null;
        if (encodedPassword==null||encodedPassword.isBlank()||encodedPassword.isEmpty())
            return false;
        try {
            password= new String(Base64.getDecoder().decode(encodedPassword));

        } catch (Exception e) {
            log.error("Exception Occured while decoding the password:"+e);
        }
        if (Pattern.matches(PASSWORD_PATTERN,password))
            return true;
        return false;
    }
}
