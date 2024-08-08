package com.springapi.demo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    OK(200),
    NOT_FOUND(404),
    INTERNAL_ERROR(500);
    
    private Integer status;
    
}
