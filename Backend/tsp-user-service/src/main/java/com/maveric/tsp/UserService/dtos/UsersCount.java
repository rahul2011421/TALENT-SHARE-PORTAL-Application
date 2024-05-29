package com.maveric.tsp.UserService.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
@Data
@NoArgsConstructor
public class UsersCount implements Serializable {
    private long activeUsers;
    private long userGroupsCount;
    private long unavailableUsers;
}