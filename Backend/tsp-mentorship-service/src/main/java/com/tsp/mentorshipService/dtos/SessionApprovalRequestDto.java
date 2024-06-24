package com.tsp.mentorshipService.dtos;

import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionApprovalRequestDto implements Serializable {

    @NonNull
    private long sessionId;
    @NonNull
    private String actionBy;
    @NonNull
    private boolean approvalstatus;
    private String Comments;
    private String optionDate1;
    private String optionDate2;

}
