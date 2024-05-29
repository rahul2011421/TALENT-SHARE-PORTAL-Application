package com.maveric.tsp.mentorshipService.dtos;
import com.maveric.tsp.mentorshipService.entities.MentorshipStatus;
import com.maveric.tsp.mentorshipService.entities.RequestStatus;
import com.maveric.tsp.mentorshipService.entities.SessionStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorSessionDto  implements Serializable {

    @NotNull
    private Long sessionId;
    @Email
    private String mentorEmailId;
    @Email
    private String menteeEmailId;
    @NonNull
    private String sessionTopic;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Boolean managerApproval;
    private Boolean mentorApproval;
    private String managerComments;
    private String requestCurrentlyWith;
    private String mentorComments;
    private SessionStatus sessionStatus;
    private RequestStatus requestStatus;
    private MentorshipStatus mentorshipStatus;
    private String mentorName;
    private String menteeName;
    private LocalDate updatedDate;
    private LocalDateTime availableDate_1;
    private LocalDateTime availableDate_2;
    private LocalDateTime scheduledDate;
}

