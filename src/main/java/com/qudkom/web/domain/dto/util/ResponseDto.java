package com.qudkom.web.domain.dto.util;

import lombok.*;

import java.util.List;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Builder
@Data
public class ResponseDto {
    private int code;
    private String message;
    private String url;
    private boolean hasErrors;
    List<LoadedError> errors;
    private Object responseObject;
    @Builder
    public ResponseDto(int code, String message, String url, boolean hasErrors, List<LoadedError> errors, Object responseObject) {
        this.code = code;
        this.message = message;
        this.url = url;
        this.hasErrors = hasErrors;
        this.errors = errors;
        this.responseObject=responseObject;
    }
}