package com.maveric.tsp.mentorshipService.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ScheduledDateDto implements Serializable {

    @NotNull
    private Long sessionId;

    @NotNull
    private String sessionScheduledDate;
}
