package com.springapi.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseObject {
    private Object data;
    private Integer status;
    private String time;
}
