package com.qudkom.web.domain.dto.util;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageKey{
    private String key;
    private String field;
    private Object[] params;
    private String defalutMsg;

    @Builder
    public MessageKey(String key, String field, Object[] params, String defalutMsg) {
        this.key = key;
        this.field = field;
        this.params = params;
        this.defalutMsg = defalutMsg;
    }
}