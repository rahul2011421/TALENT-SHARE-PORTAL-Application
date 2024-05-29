package com.maveric.tsp.mentorshipService.service;

import com.maveric.tsp.mentorshipService.dtos.*;

import java.util.List;

public interface IMentorshipService {
    String raiseRequestForMentorship (MentorshipRequestDto mentorshipRequestDto);

    public List<MentorSessionDto> getSessionDetails(String emailId, String fromDate, String toDate);
    List<MentorSessionDto> getMentorshipRequests(String mentorEmail);
    String mentorshipApproval(SessionApprovalRequestDto sessionApprovalRequestDto);
    String sessionConclusion(SessionConclusionDto sessionConclusionDto);
    String scheduleDateForSession(ScheduledDateDto scheduledDateDto);

}
