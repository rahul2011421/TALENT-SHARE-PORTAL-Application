package com.maveric.tsp.UserService.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
@Data
@NoArgsConstructor
public class MasterDataResponseDto implements Serializable {
    private List<String> businessUnits;
    private List<String> userGroups;
}
