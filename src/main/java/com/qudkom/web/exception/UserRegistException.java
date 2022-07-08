package com.qudkom.web.exception;

import com.qudkom.web.domain.dto.util.MessageKey;
import lombok.Getter;

import java.util.List;

public class UserRegistException extends Exception {
    @Getter
    private List<MessageKey> messageKeys;
    public UserRegistException(List<MessageKey> messageKeys){
        this.messageKeys=messageKeys;
    }
}
