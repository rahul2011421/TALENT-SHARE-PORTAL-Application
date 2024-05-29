package com.maveric.tsp.UserService.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor@Builder
public class RegisterDto {

        private String firstName;
        private String lastName;
        private String emailId;
        private Long empId;
        private String userGroup;
        private String businessUnit;
        private String createdBy;
}
