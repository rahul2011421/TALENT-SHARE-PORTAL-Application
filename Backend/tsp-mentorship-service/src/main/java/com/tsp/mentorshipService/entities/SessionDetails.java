package com.tsp.mentorshipService.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    @Column(nullable = false)
    private String mentorEmailId;
    @Column(nullable = false)
    private String menteeEmailId;
    @Column(nullable = false)
    private String managerEmailId;
    @Column(nullable = false)
    private String sessionTopic;
    /**
     * Requested date for a mentorship session.
     */
    @Column
    private LocalDateTime fromDate;
    @Column
    private LocalDateTime toDate;

    private Boolean managerApproval;

    private Boolean mentorApproval;
    @Column
    private String managerComments;
    @Column
    private String mentorComments;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;//Request/Scheduled/completed/Rejected.
    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus; //accepted/decliend
    @Column
    @Enumerated(EnumType.STRING)
    private MentorshipStatus mentorshipStatus; //active/closed
    @Column
    private Integer totalSessionCovered;
    @Column
    private Integer sessionDuration;
    @Column
    private LocalDate startDate;
    @Column
    private String requestCurrentlyWith;
    @Column
    @CreationTimestamp
    private LocalDate createdDate;
    @Column
    private String createdBy;
    @Column
    @UpdateTimestamp
    private LocalDate updatedDate;
    @Column
    private String updatedBy;
    @Column
    private LocalDateTime availableDate_1;
    @Column
    private LocalDateTime availableDate_2;
    @Column
    private LocalDateTime scheduledDate;
}
