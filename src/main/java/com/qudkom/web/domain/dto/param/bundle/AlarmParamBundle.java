package com.qudkom.web.domain.dto.param.bundle;

import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.vo.user.alarm.Alarm;
import lombok.Builder;
import lombok.Data;

@Data
public class AlarmParamBundle {
    private Alarm alarm;
    private Paging paging;

    public AlarmParamBundle(){}

    @Builder
    public AlarmParamBundle(Alarm alarm, Paging paging) {
        this.alarm = alarm;
        this.paging = paging;
    }
}
