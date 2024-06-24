package com.tsp.mentorshipService.mappers;

import com.tsp.mentorshipService.dtos.MentorSessionDto;
import com.tsp.mentorshipService.dtos.MentorshipRequestDto;
import com.tsp.mentorshipService.dtos.SessionConclusionDto;
import com.tsp.mentorshipService.entities.SessionDetails;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T18:27:03+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class MentorshipRequestMapperImpl implements MentorshipRequestMapper {

    @Override
    public SessionDetails fromMentorshipRequestDtoToSessionDetails(MentorshipRequestDto mentorshipRequestDto) {
        if ( mentorshipRequestDto == null ) {
            return null;
        }

        SessionDetails.SessionDetailsBuilder sessionDetails = SessionDetails.builder();

        sessionDetails.mentorEmailId( mentorshipRequestDto.getMentorEmailId() );
        sessionDetails.menteeEmailId( mentorshipRequestDto.getMenteeEmailId() );
        sessionDetails.sessionTopic( mentorshipRequestDto.getSessionTopic() );

        return sessionDetails.build();
    }

    @Override
    public MentorSessionDto fromSessionDetailsEntityToMentorSessionDto(SessionDetails sessionDetails) {
        if ( sessionDetails == null ) {
            return null;
        }

        MentorSessionDto mentorSessionDto = new MentorSessionDto();

        mentorSessionDto.setSessionStatus( sessionDetails.getSessionStatus() );
        mentorSessionDto.setSessionId( sessionDetails.getSessionId() );
        mentorSessionDto.setMentorEmailId( sessionDetails.getMentorEmailId() );
        mentorSessionDto.setMenteeEmailId( sessionDetails.getMenteeEmailId() );
        mentorSessionDto.setSessionTopic( sessionDetails.getSessionTopic() );
        mentorSessionDto.setFromDate( sessionDetails.getFromDate() );
        mentorSessionDto.setToDate( sessionDetails.getToDate() );
        mentorSessionDto.setManagerApproval( sessionDetails.getManagerApproval() );
        mentorSessionDto.setMentorApproval( sessionDetails.getMentorApproval() );
        mentorSessionDto.setManagerComments( sessionDetails.getManagerComments() );
        mentorSessionDto.setRequestCurrentlyWith( sessionDetails.getRequestCurrentlyWith() );
        mentorSessionDto.setMentorComments( sessionDetails.getMentorComments() );
        mentorSessionDto.setRequestStatus( sessionDetails.getRequestStatus() );
        mentorSessionDto.setMentorshipStatus( sessionDetails.getMentorshipStatus() );
        mentorSessionDto.setUpdatedDate( sessionDetails.getUpdatedDate() );
        mentorSessionDto.setAvailableDate_1( sessionDetails.getAvailableDate_1() );
        mentorSessionDto.setAvailableDate_2( sessionDetails.getAvailableDate_2() );
        mentorSessionDto.setScheduledDate( sessionDetails.getScheduledDate() );

        return mentorSessionDto;
    }

    @Override
    public SessionDetails fromSessionConclusionDtoToSessionDetailsEntity(SessionConclusionDto sessionConclusionDto) {
        if ( sessionConclusionDto == null ) {
            return null;
        }

        SessionDetails.SessionDetailsBuilder sessionDetails = SessionDetails.builder();

        sessionDetails.sessionId( sessionConclusionDto.getSessionId() );
        sessionDetails.sessionTopic( sessionConclusionDto.getSessionTopic() );
        sessionDetails.totalSessionCovered( sessionConclusionDto.getTotalSessionCovered() );
        sessionDetails.sessionDuration( sessionConclusionDto.getSessionDuration() );
        sessionDetails.startDate( sessionConclusionDto.getStartDate() );

        return sessionDetails.build();
    }
}
