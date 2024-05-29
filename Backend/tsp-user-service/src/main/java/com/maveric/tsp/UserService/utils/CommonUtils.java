package com.maveric.tsp.UserService.utils;

import com.maveric.tsp.UserService.config.MasterData;
import com.maveric.tsp.UserService.dtos.UserProfileUpdateDto;
import com.maveric.tsp.UserService.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class CommonUtils {
    @Autowired
    private MasterData masterData;

    public void validateData(UserProfileUpdateDto userProfileUpdateRequestDto) {
        if (masterData.getSkillcategories().stream().noneMatch(categeory->categeory.equalsIgnoreCase(userProfileUpdateRequestDto.getSkillCategory())))
           throw new InvalidInputException("Invalid SkillCategeory Entered:"+userProfileUpdateRequestDto.getSkillCategory());
        userProfileUpdateRequestDto.getTechnicalSkillList()
                .stream().map(skill->{
                    if (masterData.getTechnicalskills().stream().noneMatch(techSkil->techSkil.equalsIgnoreCase(skill)))
                        throw new InvalidInputException("Invalid TechnicalSkill Entered:"+skill);
                    return skill;
                }).collect(Collectors.toList());
        userProfileUpdateRequestDto.getDomainList()
                .stream().map(domain->{
                    if (masterData.getDomains().stream().noneMatch(domainName->domainName.equalsIgnoreCase(domainName)))
                        throw new InvalidInputException("Invalid Domain Entered:"+domain);
                    return domain;
                }).collect(Collectors.toList());
    }
}
