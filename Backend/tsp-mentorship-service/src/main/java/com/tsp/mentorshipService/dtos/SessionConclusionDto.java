package com.tsp.mentorshipService.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SessionConclusionDto {

    @NonNull
    private Long sessionId;
    @NotNull
    private Integer totalSessionCovered;
    @NotNull
    private Integer sessionDuration;
    @NotNull
    private String sessionTopic;
    @NotNull
    private LocalDate startDate;
}

