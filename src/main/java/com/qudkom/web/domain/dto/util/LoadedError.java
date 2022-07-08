package com.qudkom.web.domain.dto.util;

import lombok.*;

@NoArgsConstructor
@Data
public class LoadedError {
//    private int status;
//    private int code;
    private String field;
    private String message;

    @Builder
    public LoadedError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
