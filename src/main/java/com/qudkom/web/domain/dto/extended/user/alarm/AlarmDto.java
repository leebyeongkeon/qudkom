package com.qudkom.web.domain.dto.extended.user.alarm;

import com.qudkom.web.domain.vo.user.alarm.Alarm;
import lombok.Data;

@Data
public class AlarmDto extends Alarm {
    private String boardName;
    private String title;
    private String nickname;
    private String content;
//    private String alarmClassName;
}
