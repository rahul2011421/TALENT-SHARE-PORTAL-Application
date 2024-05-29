package com.maveric.tsp.registerservice.dto;

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

        @NotEmpty(message = "Name is mandatory, should not be null or empty")
        private String firstName;
        @NotEmpty(message = "Name is mandatory, should not be null or empty")
        private String lastName;

        @NotEmpty(message = "MailId is mandatory, should not be null or empty")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@maveric-systems\\.com$",message = "Invalid email domain. Only Maveric mail id is allowed.")
        private String emailId;

        @NotNull(message = "Employee Id is mandatory, should not be empty")
        private Long empId;

        @NotEmpty(message = "userGroup details must provide")
        private String userGroup;

        @NotEmpty(message = "bu details must provide" )
        private String businessUnit;

        @NotEmpty(message = "CreatedBy should not be empty")
        private String createdBy;
}
