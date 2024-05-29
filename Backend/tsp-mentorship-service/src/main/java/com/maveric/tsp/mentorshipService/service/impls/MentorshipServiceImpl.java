package com.maveric.tsp.mentorshipService.service.impls;

import com.maveric.tsp.mentorshipService.client.NotificationClient;
import com.maveric.tsp.mentorshipService.client.UserSeriveClient;
import com.maveric.tsp.mentorshipService.dtos.*;
import com.maveric.tsp.mentorshipService.entities.MentorshipStatus;
import com.maveric.tsp.mentorshipService.entities.SessionStatus;
import com.maveric.tsp.mentorshipService.entities.SessionDetails;
import com.maveric.tsp.mentorshipService.entities.Action;
import com.maveric.tsp.mentorshipService.exceptions.NoDetailsFoundException;
import com.maveric.tsp.mentorshipService.exceptions.NoManagerFoundException;
import com.maveric.tsp.mentorshipService.exceptions.NoMentorApprovalException;
import com.maveric.tsp.mentorshipService.exceptions.NoUserFoundException;
import com.maveric.tsp.mentorshipService.mappers.MentorshipRequestMapper;
import com.maveric.tsp.mentorshipService.repositories.MentorshipServiceRepo;
import com.maveric.tsp.mentorshipService.service.IMentorshipService;
import com.maveric.tsp.mentorshipService.utils.CommonUtils;
import com.maveric.tsp.mentorshipService.utils.Constants;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MentorshipServiceImpl implements IMentorshipService {

    @Autowired
    MentorshipServiceRepo mentorshipServiceRepo;
    @Autowired
    MentorshipRequestMapper mentorshipRequestMapper;

    @Value("${tsp.calendar.blockingcontent.location}")
    private String location;

    @Value("${tsp.calendar.blockingcontent.content}")
    private String content;

    @Value("${tsp.calendar.blockingcontent.timezone}")
    private String timeZone;

    @Value("${tsp.calendar.blockingcontent.endminutes}")
    private int minutes;

    @Autowired
    UserSeriveClient userSeriveClient;

    @Autowired
    NotificationClient notificationClient;

    @Autowired
    CommonUtils commonUtils;

    @Value("${tsp.email.set-subject-approval}")
    private String setSubjectApproval;

    @Value("${tsp.email.set-subject-rejected}")
    private String setSubjectRejection;

    @Value("${tsp.email.set-subject-approved}")
    private String SetSubjectApproved;

    @Value("${tsp.email.approval-body}")
    private String approvalBody;

    @Value("${tsp.email.session-approved-notification-to-mentee}")
    private String sessionApprovedMentee;

    @Value("${tsp.email.session-approved-notification-to-mentee-manager}")
    private String sessionApprovedMenteeManager;

    @Value("${tsp.email.session-rejected-notification-to-mentee}")
    private String rejectedBodyMentee;



    /**
     * Raising mentorship request
     */
    @Override
    public String raiseRequestForMentorship(MentorshipRequestDto mentorshipRequestDto) {
        SessionDetails sessionDetails = mentorshipRequestMapper.fromMentorshipRequestDtoToSessionDetails(mentorshipRequestDto);
        sessionDetails.setSessionStatus(SessionStatus.REQUESTED);
        sessionDetails.setMentorshipStatus(MentorshipStatus.ACTIVE);
        sessionDetails.setRequestCurrentlyWith(Constants.MANAGER);
        sessionDetails.setFromDate(LocalDateTime.now());
        sessionDetails.setCreatedBy(mentorshipRequestDto.getMenteeEmailId());
        sessionDetails.setManagerEmailId(getManagerMailId(mentorshipRequestDto.getMentorEmailId()));
        log.info("MentorSession Request" + sessionDetails);
        mentorshipServiceRepo.save(sessionDetails);
        sendMailNotificationForManager(sessionDetails);
        log.info("Saved mentorship data successfully");
        return Constants.REQUEST_RAISED;
    }

    /*
     * Sending mail notification to the mentor's manager regarding session rasied by mentee*/

    private void sendMailNotificationForManager(SessionDetails sessionDetails) {
        try {
            String managerMailId = getManagerMailId(sessionDetails.getMentorEmailId());
            UserProfileResponseDto userProfileResponseDto = getUserDetailsByEmailId(sessionDetails.getMenteeEmailId());
            String name = userProfileResponseDto.getFirstName().concat(" " + userProfileResponseDto.getLastName());
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setSubject(setSubjectApproval);
            emailRequest.setRecipient(managerMailId);
            String managerName = managerMailId.substring(0, managerMailId.indexOf("@"));
            String profileSummary = userProfileResponseDto.getProfessionalSummary() != null ? userProfileResponseDto.getProfessionalSummary() : "";
            String emailBody= approvalBody.replace("<recipientName>",managerName)
                    .replace("<mailTo>",Constants.MANAGER)
                    .replace("<mentee>",name)
                            .replace("<professionalSummary>",profileSummary);
            emailRequest.setMessage(emailBody);
            notificationClient.sendEmail(emailRequest);

        } catch (Exception e) {
            log.error(Constants.LOG_EMAIL_ISSUE + e.getMessage());
        }
    }

    /**
     * Menthod to get the mail id of the mentors manager from user service using mentor email id
     */
    private String getManagerMailId(String emailId) {
        ResponseEntity<ResponseDto<UserProfileResponseDto>> response = userSeriveClient.getUserProfileByEmailId(emailId);
        log.info("RESPONSE FROM USER_SERVICE:" + response.getBody());
        ResponseDto<UserProfileResponseDto> responseDto = response.getBody();
        UserProfileResponseDto userProfileResponseDto = responseDto.getPayLoad();
        if (userProfileResponseDto.getManagerMailId() == null) {
            throw new NoManagerFoundException(Constants.NO_MANAGER_FOUND_EXCEPTION);
        }
        return userProfileResponseDto.getManagerMailId();
    }


    private UserProfileResponseDto getUserDetailsByEmailId(String emailId) {
        ResponseEntity<ResponseDto<UserProfileResponseDto>> userdetails = userSeriveClient.getUserProfileByEmailId(emailId);
        if (userdetails == null) {
            throw new NoUserFoundException(Constants.NO_USER_FOUND_EXCEPTION);
        }
        ResponseDto<UserProfileResponseDto> responseDto = userdetails.getBody();
        return responseDto.getPayLoad();
    }


    /*
     *  Email notification for mentor approval*/


    private void sendMailNotificationForMentor(SessionDetails sessionDetails) {
        try {
            UserProfileResponseDto userProfileResponseDto = getUserDetailsByEmailId(sessionDetails.getMenteeEmailId());
            String name = userProfileResponseDto.getFirstName().concat(" " + userProfileResponseDto.getLastName());
            log.info(sessionDetails.getMentorEmailId());
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setSubject(setSubjectApproval);
            emailRequest.setRecipient(sessionDetails.getMentorEmailId());
            String mentorName = sessionDetails.getMentorEmailId().substring(0, sessionDetails.getMentorEmailId().indexOf("@"));
            String profileSummary = userProfileResponseDto.getProfessionalSummary() != null ? userProfileResponseDto.getProfessionalSummary() : "";
            String emailBody= approvalBody.replace("<recipientName>",mentorName)
                    .replace("<mailTo>",Constants.MENTOR)
                    .replace("<mentee>",name)
                    .replace("<professionalSummary>",profileSummary);
            emailRequest.setMessage(emailBody);
            notificationClient.sendEmail(emailRequest);

        } catch (Exception e) {
            log.error(Constants.LOG_EMAIL_ISSUE + e.getMessage());
        }
    }

    private void sendMailNotificationToMentee(SessionDetails sessionDetails) {
        try {
            if (sessionDetails.getMentorApproval()!=null && sessionDetails.getMentorApproval()==true) {
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setSubject(SetSubjectApproved);
                emailRequest.setRecipient(sessionDetails.getMenteeEmailId());
                String menteeName =getName(sessionDetails.getMenteeEmailId());
                String emailBody= sessionApprovedMentee.replace("<recipientName>",menteeName)
                                .replace("<comments>","");
                emailRequest.setMessage(emailBody);
                notificationClient.sendEmail(emailRequest);
            } else if (sessionDetails.getManagerApproval()!=null && sessionDetails.getManagerApproval()==false) {
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setSubject(setSubjectRejection +Constants.MANAGER);
                emailRequest.setRecipient(sessionDetails.getMenteeEmailId());
                String menteeName=getName(sessionDetails.getMenteeEmailId());
                String emailBody= rejectedBodyMentee.replace("<recipientName>",menteeName)
                        .replace("<comments>",sessionDetails.getManagerComments());
                emailRequest.setMessage(emailBody);
                notificationClient.sendEmail(emailRequest);
            } else if ( sessionDetails.getMentorApproval()!=null && sessionDetails.getMentorApproval()==false) {
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setSubject(setSubjectRejection +Constants.MENTOR);
                emailRequest.setRecipient(sessionDetails.getMenteeEmailId());
                String menteeName=getName(sessionDetails.getMenteeEmailId());
                String emailBody= rejectedBodyMentee.replace("<recipientName>",menteeName)
                        .replace("<comments>",sessionDetails.getMentorComments());
                emailRequest.setMessage(emailBody);
                notificationClient.sendEmail(emailRequest);
            }
        } catch (Exception e) {
            log.error(setSubjectApproval + e.getMessage());
        }
    }

    private void sendMailNotificationToMenteeManager(SessionDetails sessionDetails) {
        try {
            String menteeManager = getManagerMailId(sessionDetails.getMenteeEmailId());
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setSubject(SetSubjectApproved);
            emailRequest.setRecipient(menteeManager);
            String menteeName=getName(sessionDetails.getMenteeEmailId());
            String managerName=getName(sessionDetails.getManagerEmailId());
            String emailBody= sessionApprovedMenteeManager.replace("<recipientName>",managerName)
                    .replace("<sessionTopic>",sessionDetails.getSessionTopic())
                    .replace("<mentee>",menteeName)
                    .replace("<mentorEmailid>",sessionDetails.getMentorEmailId());
            emailRequest.setMessage(emailBody);
            notificationClient.sendEmail(emailRequest);

        } catch (Exception e) {
            log.error(Constants.LOG_EMAIL_ISSUE + e.getMessage());
        }
    }

    /****/

    @Override
    public List<MentorSessionDto> getMentorshipRequests(String emailId) {
        log.info("MAIL ID TO FETCH REQUESTS:" + emailId);
        UserProfileResponseDto userProfileResponseDto = getUserDetailsByEmailId(emailId);
       if (userProfileResponseDto.getUserGroup().equalsIgnoreCase(Constants.MENTOR)) {
           List<MentorSessionDto> mentorSessionDtos=getMentorshipRequestsForMentor(emailId);
           return mentorSessionDtos;
       }
       if (userProfileResponseDto.getUserGroup().equalsIgnoreCase(Constants.MANAGER)){
            List<SessionDetails> sessionDetails = mentorshipServiceRepo.findByManagerEmailId(emailId);
           log.info("mentorshipRequests" + sessionDetails);
           return sessionDetails.stream().filter(session -> session.getMentorshipStatus().equals(MentorshipStatus.ACTIVE)).filter(session -> session.getManagerApproval()==null).map(this::mapSessionDetailsToDto).toList();

       }
        return null;
    }

    private  List<MentorSessionDto> getMentorshipRequestsForMentor(String mentorEmailId){
        List<SessionDetails> sessionDetails =
                mentorshipServiceRepo.findByMentorEmailId(mentorEmailId);
        if(sessionDetails==null) {
            throw new NoDetailsFoundException(Constants.NO_DETAILS_FOUND_EXCEPTION);
        }
            return sessionDetails.stream()
                    .filter(session -> session.getManagerApproval() !=null && session.getManagerApproval()==true)
                    .filter(session -> session.getMentorApproval() == null)
                    .filter(session -> session.getMentorshipStatus().equals(MentorshipStatus.ACTIVE))
                    .map(this::mapSessionDetailsToDto).toList();

    }




    private MentorSessionDto mapSessionDetailsToDto(SessionDetails sessionDetails) {
        return mentorshipRequestMapper.fromSessionDetailsEntityToMentorSessionDto(sessionDetails);

    }
    /*
     * The approval of session request by mentor and manager*/

    @Override
    public String mentorshipApproval(SessionApprovalRequestDto sessionApprovalRequestDto) {

        Optional<SessionDetails> optionalRequest = Optional.ofNullable(mentorshipServiceRepo.findById(sessionApprovalRequestDto.getSessionId())
                .orElseThrow(()->new NoDetailsFoundException(Constants.NO_DETAILS_FOUND_EXCEPTION+sessionApprovalRequestDto.getSessionId())));

        SessionDetails sessionDetails = optionalRequest.get();
        Action action = Action.valueOf(sessionApprovalRequestDto.getActionBy().toUpperCase());
        switch (action) {
            case MENTOR_ACCEPT:
                return handleMentorAccept(sessionDetails, sessionApprovalRequestDto);

            case MANAGER_ACCEPT:
                return handleManagerAccept(sessionDetails, sessionApprovalRequestDto);

            case MENTOR_REJECT:
                log.info("GEt into mentor reject");
                return handleMentorReject(sessionDetails, sessionApprovalRequestDto);

            case MANAGER_REJECT:
                log.info("geot inot manager reject");
                return handleManagerReject(sessionDetails, sessionApprovalRequestDto);

            default:
                return Constants.INVALID_ACTION;
        }
    }

    private String handleMentorAccept(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto) {
        SessionDetails updatedSessionDetails = commonUtils.handleMentorAccept(sessionDetails, sessionApprovalRequestDto);
        log.info(updatedSessionDetails.toString());
        mentorshipServiceRepo.save(updatedSessionDetails);
        sendMailNotificationToMentee(updatedSessionDetails);
        sendMailNotificationToMenteeManager(updatedSessionDetails);
        return Constants.MENTOR + " " + Constants.APPROVAL_SUCCESS;
    }

    private String handleManagerAccept(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto) {
        SessionDetails updatedSessionDetails = commonUtils.handleManagerAccept(sessionDetails, sessionApprovalRequestDto);
        mentorshipServiceRepo.save(updatedSessionDetails);
        sendMailNotificationForMentor(updatedSessionDetails);
        log.info(updatedSessionDetails.toString());
        return Constants.MANAGER + " " + Constants.APPROVAL_SUCCESS;

    }

    private String handleMentorReject(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto) {
        log.info("get into mentor reject method " + sessionDetails + " " + sessionApprovalRequestDto);
        SessionDetails updatedSessionDetails = commonUtils.handleMentorReject(sessionDetails, sessionApprovalRequestDto);
        mentorshipServiceRepo.save(updatedSessionDetails);
        sendMailNotificationToMentee(updatedSessionDetails);
        return Constants.MENTOR + " " + Constants.APPROVAL_REJECTED;
    }

    private String handleManagerReject(SessionDetails sessionDetails, SessionApprovalRequestDto sessionApprovalRequestDto) {
        log.info("get into manager reject method " + sessionDetails + " " + sessionApprovalRequestDto);
        SessionDetails updatedSessionDetails = commonUtils.handleManagerReject(sessionDetails, sessionApprovalRequestDto);
        mentorshipServiceRepo.save(updatedSessionDetails);
        sendMailNotificationToMentee(updatedSessionDetails);
        return Constants.MANAGER + " " + Constants.APPROVAL_REJECTED;
    }

    @Override
    public List<MentorSessionDto> getSessionDetails(String emailId, String fromDate, String toDate) {
        LocalDate localStartDate = commonUtils.sessionDetailsHistoryDate(fromDate);
        LocalDate localToDate = commonUtils.sessionDetailsHistoryDate(toDate);
        UserProfileResponseDto userProfileResponseDto = getUserDetailsByEmailId(emailId);
        log.info("details of the user" + userProfileResponseDto);
        List<MentorSessionDto> mentorshipDTOs = new ArrayList<>();
        if (userProfileResponseDto.getUserGroup().equalsIgnoreCase(Constants.MENTEE)) {
            List<SessionDetails> mentorships = mentorshipServiceRepo.findByMenteeEmailIdAndFromDateBetween(emailId,
                    localStartDate.atStartOfDay(), localToDate.atTime(LocalTime.MAX));
            mentorshipDTOs = fetchDetailsForSessionHistory(mentorships, userProfileResponseDto);
            log.info("all session details for history for mentee" + mentorshipDTOs);
            return mentorshipDTOs;
        }
        if (userProfileResponseDto.getUserGroup().equalsIgnoreCase(Constants.MENTOR)) {
            List<SessionDetails> mentorships = mentorshipServiceRepo.findByMentorEmailIdAndFromDateBetween(emailId,
                    localStartDate.atStartOfDay(), localToDate.atTime(LocalTime.MAX));
            mentorshipDTOs = fetchDetailsForSessionHistoryForMentor(mentorships, userProfileResponseDto);
            log.info("all session details for history for mentor" + mentorshipDTOs);
            return mentorshipDTOs;
        }
        if (userProfileResponseDto.getUserGroup().equalsIgnoreCase(Constants.MANAGER)) {
            List<SessionDetails> mentorships = mentorshipServiceRepo.findByManagerEmailIdAndFromDateBetween(emailId, localStartDate.atStartOfDay(), localToDate.atTime(LocalTime.MAX));
            mentorshipDTOs = fetchDetailsForSessionHistory(mentorships, userProfileResponseDto);
            log.info("all session details for history for manager" + mentorshipDTOs);
            return mentorshipDTOs;
        }
        log.info("all session details for history" + mentorshipDTOs);
        return mentorshipDTOs;
    }

    private String getName(String emailID) {
        UserProfileResponseDto userProfileResponseDto = getUserDetailsByEmailId(emailID);
        return userProfileResponseDto.getFirstName().concat(" " + userProfileResponseDto.getLastName());
    }

    private List<MentorSessionDto> fetchDetailsForSessionHistory(List<SessionDetails> sessionDetails, UserProfileResponseDto userProfileResponseDto) {
        List<MentorSessionDto> mentorshipDTOs = new ArrayList<>();
        for (SessionDetails sessionDetail : sessionDetails) {
               MentorSessionDto mentorshipDTO = listOfSessionDetailsForHistory(sessionDetail,userProfileResponseDto);
            mentorshipDTOs.add(mentorshipDTO);
        }
        return mentorshipDTOs;
    }

    private MentorSessionDto listOfSessionDetailsForHistory(SessionDetails sessionDetail, UserProfileResponseDto userProfileResponseDto){
        MentorSessionDto mentorshipDTO = new MentorSessionDto();
        BeanUtils.copyProperties(sessionDetail, mentorshipDTO);
        mentorshipDTO.setMentorName(getName(sessionDetail.getMentorEmailId()));
        mentorshipDTO.setMenteeName(getName(sessionDetail.getMenteeEmailId()));
        log.info(mentorshipDTO.toString());
        return mentorshipDTO;
    }
    private List<MentorSessionDto> fetchDetailsForSessionHistoryForMentor(List<SessionDetails> sessionDetails, UserProfileResponseDto userProfileResponseDto) {
        List<MentorSessionDto> mentorshipDTOs = new ArrayList<>();
        for (SessionDetails sessionDetail : sessionDetails) {
          if (sessionDetail.getManagerApproval()!=null && sessionDetail.getManagerApproval() == true) {
           MentorSessionDto mentorshipDTO = listOfSessionDetailsForHistory(sessionDetail,userProfileResponseDto);
              mentorshipDTOs.add(mentorshipDTO);
          }
        }
        return mentorshipDTOs;
    }

    public String sessionConclusion(SessionConclusionDto sessionConclusionDto) {
        Optional<SessionDetails> sessionDetails= Optional.ofNullable(mentorshipServiceRepo.findById(sessionConclusionDto.getSessionId())
                .orElseThrow(()->new NoDetailsFoundException(Constants.NO_DETAILS_FOUND_EXCEPTION)));
        if(sessionDetails.get().getMentorApproval()!=null && sessionDetails.get().getMentorApproval()==false){
            throw new NoMentorApprovalException(Constants.NO_MENTOR_APPROVAL);
        }
        SessionDetails sessionDetails1 = sessionDetails.get();
        sessionDetails1.setSessionStatus(SessionStatus.COMPLETED);
        sessionDetails1.setMentorshipStatus(MentorshipStatus.CLOSED);
        sessionDetails1.setToDate(LocalDateTime.now());
        sessionDetails1.setUpdatedBy(sessionDetails.get().getMenteeEmailId());
        sessionDetails1.setUpdatedDate(LocalDate.now());
        BeanUtils.copyProperties(sessionConclusionDto, sessionDetails1);
        mentorshipServiceRepo.save(sessionDetails1);
        return Constants.SESSION_CONCLUSION;
    }


    /**
     *
     * @param scheduledDateDto
     * @return String
     * @throws FeignException
     */

    @Override
    public String scheduleDateForSession(ScheduledDateDto scheduledDateDto) {
        Optional<SessionDetails> optionalsession = Optional.ofNullable(mentorshipServiceRepo.findById(scheduledDateDto.getSessionId())
                .orElseThrow(() -> new NoDetailsFoundException(Constants.NO_DETAILS_FOUND_EXCEPTION)));
        CalendarBlockingInfoDto calendarBlockingInfoDto = new CalendarBlockingInfoDto();
        SessionDetails sessionDetails = optionalsession.get();
        sessionDetails.setSessionStatus(SessionStatus.SCHEDULED);
        ResponseEntity<ResponseDto> message;
        LocalDateTime sessionScheduledDate = commonUtils.dateFormatterForCalendarBlocking(scheduledDateDto.getSessionScheduledDate());
        sessionDetails.setScheduledDate(sessionScheduledDate);
        mentorshipServiceRepo.save(sessionDetails);
        calendarBlockingInfoDto.setMentorEmail(sessionDetails.getMentorEmailId());
        calendarBlockingInfoDto.setMenteeEmail(sessionDetails.getMenteeEmailId());
        calendarBlockingInfoDto.setSessionTopic(sessionDetails.getSessionTopic());
        calendarBlockingInfoDto.setFromDateAndTime(sessionScheduledDate);
        /**
            set endDateAndTime to dto by adding some minutes to fromDateAndTime thorugh yml file
         */
        calendarBlockingInfoDto.setEndDateAndTime(sessionScheduledDate.plusMinutes(minutes));
        calendarBlockingInfoDto.setContent(content + " " + sessionDetails.getSessionTopic());
        calendarBlockingInfoDto.setLocation(location);
        calendarBlockingInfoDto.setFromTimeZone(timeZone);
        calendarBlockingInfoDto.setEndTimeZone(timeZone);
        try{
            message = notificationClient.blockCalendar(calendarBlockingInfoDto);
            return message.getBody().getPayLoad().toString();
        }catch (FeignException exception){
            log.info("Feign Exception occured :"+exception.getMessage());
        }
        return null;
    }


}
