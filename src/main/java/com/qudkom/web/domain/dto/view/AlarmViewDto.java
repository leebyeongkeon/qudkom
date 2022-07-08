package com.qudkom.web.domain.dto.view;

import com.qudkom.web.domain.dto.extended.user.alarm.AlarmDto;
import com.qudkom.web.domain.dto.view.handler.AlarmPageHandler;
import lombok.Data;

import java.util.List;

@Data
public class AlarmViewDto {
    private List<AlarmDto> alarmList;
    private AlarmPageHandler alarmPageHandler;
}
