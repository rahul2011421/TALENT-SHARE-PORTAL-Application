package com.maveric.tsp.registerservice.entities;

import com.maveric.tsp.registerservice.validation.PasswordValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "userDetails")
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empId",nullable = false)
    private Long empId;

    @Column(name = "emailId",nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@maveric-systems\\.com$",message = "Invalid email domain. Only Maveric mail id is allowed.")
    private String emailId;

    @Column(name = "firstName",nullable = false)
    @Size(min = 2,max = 30,message = "First Name length should be between 2 to 30 charecters")
    private String firstName;

    @Column(name = "lastName",nullable = false)
    @Size(min = 1,max = 30,message = "Last Name length should be between 2 to 30 charecters")
    private String lastName;

    @Column(name = "user_group",nullable = false)
    private String userGroup;

    @Column(name = "business_unit",nullable = false)
    private String businessUnit;

    private boolean status;

    @PasswordValidation
    @Column(nullable = false)
    private String password;

    private boolean passwordReset;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    private LocalDate exitDate;

    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    @PrePersist
    public void prePersist(){
        status = true;
        passwordReset = true;
    }
}
