package com.maveric.tsp.mentorshipService.mappers;


import com.maveric.tsp.mentorshipService.dtos.MentorSessionDto;
import com.maveric.tsp.mentorshipService.dtos.MentorshipRequestDto;
import com.maveric.tsp.mentorshipService.dtos.SessionConclusionDto;
import com.maveric.tsp.mentorshipService.entities.SessionDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel="spring")
public interface MentorshipRequestMapper {
        SessionDetails fromMentorshipRequestDtoToSessionDetails(MentorshipRequestDto mentorshipRequestDto);

        @Mapping(source = "sessionStatus",target = "sessionStatus")
        MentorSessionDto fromSessionDetailsEntityToMentorSessionDto(SessionDetails sessionDetails);

        SessionDetails fromSessionConclusionDtoToSessionDetailsEntity(SessionConclusionDto sessionConclusionDto);
}
