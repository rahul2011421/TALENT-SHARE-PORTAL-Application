package com.maveric.tsp.registerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;

    private Integer code;

    private String message;

    private String token;

    private T payLoad = null;
}
