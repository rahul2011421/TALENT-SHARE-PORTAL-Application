package com.maveric.tsp.registerservice.util;

import com.maveric.tsp.registerservice.config.MasterData;
import com.maveric.tsp.registerservice.dto.RegisterDto;
import com.maveric.tsp.registerservice.exception.InvalidInputException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Component
public class CommonUtils {

    @Value("${tsp.password.pattern}")
    private String passwordPattern;

    @Autowired
    private MasterData masterData;
    public void validateMasterData(String userGroup,String businessUnit) {
        boolean isUserGroupPresent=masterData.getUsergroups().values().stream().anyMatch(groups->groups.contains(userGroup.toUpperCase()));
        boolean isBusinessUnitPresent=masterData.getBusinessunits().values().stream().anyMatch(businessUnits->businessUnits.contains(businessUnit.toUpperCase()));
        if (!isUserGroupPresent){
            throw new InvalidInputException("Invalid UserGroup :"+userGroup);
        }
        if (!isBusinessUnitPresent){
            throw new InvalidInputException("Invalid BusinessUnit :"+businessUnit);
        }
    }

    public LocalDate convertToLocaDateFormat(String date){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        return localDate;
    }

    public boolean validateSystemGeneratedPassword(String generatedPassword) {
            return Pattern.matches(passwordPattern,generatedPassword);
    }
}
