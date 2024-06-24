package com.tsp.mentorshipService.mappers;


import com.tsp.mentorshipService.dtos.MentorSessionDto;
import com.tsp.mentorshipService.dtos.MentorshipRequestDto;
import com.tsp.mentorshipService.dtos.SessionConclusionDto;
import com.tsp.mentorshipService.entities.SessionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel="spring")
public interface MentorshipRequestMapper {
        SessionDetails fromMentorshipRequestDtoToSessionDetails(MentorshipRequestDto mentorshipRequestDto);

        @Mapping(source = "sessionStatus",target = "sessionStatus")
        MentorSessionDto fromSessionDetailsEntityToMentorSessionDto(SessionDetails sessionDetails);

        SessionDetails fromSessionConclusionDtoToSessionDetailsEntity(SessionConclusionDto sessionConclusionDto);
}
