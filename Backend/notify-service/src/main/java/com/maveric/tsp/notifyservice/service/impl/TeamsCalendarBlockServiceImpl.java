package com.maveric.tsp.notifyservice.service.impl;

import com.maveric.tsp.notifyservice.dto.CalendarBlockingInfoDto;
import com.maveric.tsp.notifyservice.graphApiConfig.GraphApiConnection;
import com.maveric.tsp.notifyservice.service.ITeamsCalendarBlockService;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Service
@Slf4j
public class TeamsCalendarBlockServiceImpl implements ITeamsCalendarBlockService {

    @Autowired
    GraphApiConnection graphApiConnection;
    private GraphServiceClient<Request> appClient;

    @Value("${spring.mail.username}")
    private String sender;

    public String blockCalendar(CalendarBlockingInfoDto calendarBlockingInfo) {
        try {
            appClient = graphApiConnection.intializeGraphForAppOnlyAuth();
        } catch (Exception e) {
            log.error("Error Initializing Graph for user auth", e.getMessage());
            return "Error Initializing Graph for user auth";
        }

        try {
            Event event = new Event();
            event.subject = calendarBlockingInfo.getSessionTopic();
            event.body = setBody(calendarBlockingInfo.getContent());
            event.location = setLocation(calendarBlockingInfo.getLocation());
            event.start=setDateTimeZone(calendarBlockingInfo.getFromDateAndTime(), calendarBlockingInfo.getFromTimeZone());
            event.end=setDateTimeZone(calendarBlockingInfo.getEndDateAndTime(), calendarBlockingInfo.getEndTimeZone());
            event.attendees = addAttendeesToList(calendarBlockingInfo.getMentorEmail(), calendarBlockingInfo.getMenteeEmail());
            event.isOnlineMeeting = true;
            event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;

            try {
                Event result = appClient.users(sender).calendar().events().buildRequest().post(event);
                log.info("event created successfully");
                return "event created successfully";
            } catch (GraphServiceException e) {
                log.error("failed to create event");
                log.error(e.getMessage());
                return "failed to create event";
            }

        } catch (Exception e) {
            log.error("Error Adding da");
            log.error(e.getMessage());
            return "failed to create event";
        }
    }


    private Attendee addAttendee(EmailAddress emailAddressObj) {
        Attendee attendee = new Attendee();
        attendee.emailAddress = emailAddressObj;
        attendee.type = AttendeeType.REQUIRED;
        return attendee;
    }

    private EmailAddress addEmailAddress(String emailAddress) {
        EmailAddress emailAddressObj = new EmailAddress();
        emailAddressObj.address = emailAddress;
        return emailAddressObj;
    }

    private DateTimeTimeZone setDateTimeZone(LocalDateTime dateAndTime, String timeZone) {
        DateTimeTimeZone end = new DateTimeTimeZone();
        end.dateTime = dateAndTime.toString();
        end.timeZone = timeZone.toString();
        return end;
    }

    private Location setLocation(String location){
        Location locationObj = new Location();
        locationObj.displayName = location;
        return locationObj;
    }

    private ItemBody setBody(String body){
        ItemBody itemBody = new ItemBody();
        itemBody.contentType = BodyType.HTML;
        itemBody.content = body;
        return itemBody;
    }

    private LinkedList<Attendee> addAttendeesToList(String mentorEmail, String menteeEmail){
        LinkedList<Attendee> attendees = new LinkedList<Attendee>();
        attendees.add(addAttendee(addEmailAddress(mentorEmail)));
        attendees.add(addAttendee(addEmailAddress(menteeEmail)));
        return attendees;
    }

}
