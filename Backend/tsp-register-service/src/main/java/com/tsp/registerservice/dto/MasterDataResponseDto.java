package com.tsp.registerservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MasterDataResponseDto implements Serializable {
    private List<String> businessUnits;
    private List<String> userGroups;
}
