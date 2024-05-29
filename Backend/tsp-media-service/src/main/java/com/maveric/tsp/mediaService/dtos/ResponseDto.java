package com.maveric.tsp.mediaService.dtos;

import com.ctc.wstx.shaded.msv_core.datatype.SerializationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseDto<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;

    private Integer code;

    private String message;

    private T payLoad = null;




}
