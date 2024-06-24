package com.tsp.mentorshipService.dtos;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestDto implements Serializable {
    @Email
    private String mentorEmailId;
    @Email
    private String menteeEmailId;
    @NonNull
    private String sessionTopic;

}
