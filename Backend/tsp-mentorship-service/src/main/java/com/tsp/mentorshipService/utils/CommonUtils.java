package com.tsp.mentorshipService.utils;

import com.tsp.mentorshipService.dtos.SessionApprovalRequestDto;
import com.tsp.mentorshipService.entities.MentorshipStatus;
import com.tsp.mentorshipService.entities.RequestStatus;
import com.tsp.mentorshipService.entities.SessionStatus;
import com.tsp.mentorshipService.entities.SessionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class CommonUtils {

    public LocalDate sessionDetailsHistoryDate(String date){

        date=date.trim();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localdate = LocalDate.parse(date, dateFormatter);
        return localdate;
    }

    public LocalDateTime dateFormating(String date){

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localdate = LocalDateTime.parse(date, dateFormatter);
        return localdate;
    }

    public LocalDateTime dateFormatterForCalendarBlocking(String date){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = localDateTime.format(outputFormatter);
        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateTime, outputFormatter);
        return parsedDateTime;
    }



    public SessionDetails handleMentorAccept(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto){
        if (sessionDetails.getManagerApproval()!=null && sessionDetails.getManagerApproval()==true) {
            sessionDetails.setSessionStatus(SessionStatus.APPROVED);
            sessionDetails.setMentorApproval(sessionApprovalRequestDto.isApprovalstatus());
            sessionDetails.setRequestCurrentlyWith(Constants.MENTEE);
            sessionDetails.setRequestStatus(RequestStatus.ACCEPTED);
            sessionDetails.setMentorshipStatus(MentorshipStatus.ACTIVE);
            sessionDetails.setAvailableDate_1(dateFormating(sessionApprovalRequestDto.getOptionDate1()));
            sessionDetails.setAvailableDate_2(dateFormating(sessionApprovalRequestDto.getOptionDate2()));
            return sessionDetails;
        }
        return sessionDetails;
    }


    public SessionDetails handleManagerAccept(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto){
            sessionDetails.setManagerApproval(sessionApprovalRequestDto.isApprovalstatus());
            sessionDetails.setRequestCurrentlyWith(Constants.MENTOR);
            return sessionDetails;
    }

    public SessionDetails handleMentorReject(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto){
        log.info("get into mentor utility reject method "+sessionDetails+" "+sessionApprovalRequestDto);
        if (sessionDetails.getManagerApproval()==true) {
            sessionDetails=rejectionStatus(sessionDetails);
            sessionDetails.setMentorApproval(sessionApprovalRequestDto.isApprovalstatus());
            sessionDetails.setMentorComments(sessionApprovalRequestDto.getComments());
            log.info("all the updated session"+sessionDetails);
            return sessionDetails;
        }
        return sessionDetails;
    }
    public SessionDetails handleManagerReject(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto){
        log.info("get into manager utility reject method "+sessionDetails+" "+sessionApprovalRequestDto);
            sessionDetails=rejectionStatus(sessionDetails);
            sessionDetails.setManagerApproval(sessionApprovalRequestDto.isApprovalstatus());
            sessionDetails.setManagerComments(sessionApprovalRequestDto.getComments());
            log.info("all the updated session"+sessionDetails);
            return sessionDetails;
    }

    private SessionDetails rejectionStatus(SessionDetails sessionDetails){
        log.info("get into all common utility reject method "+sessionDetails);
        sessionDetails.setRequestCurrentlyWith(Constants.MENTEE);
        sessionDetails.setSessionStatus(SessionStatus.REJECTED);
        sessionDetails.setMentorshipStatus(MentorshipStatus.CLOSED);
        sessionDetails.setRequestStatus(RequestStatus.DECLINED);
        return sessionDetails;
    }

}
