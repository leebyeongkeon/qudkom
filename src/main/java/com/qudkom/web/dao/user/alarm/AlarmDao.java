package com.qudkom.web.dao.user.alarm;

import com.qudkom.web.domain.dto.extended.user.alarm.AlarmDto;
import com.qudkom.web.domain.dto.param.bundle.AlarmParamBundle;
import com.qudkom.web.domain.vo.user.alarm.Alarm;

import java.util.List;

public interface AlarmDao {
    List<AlarmDto> selectList(AlarmParamBundle alarmParamBundle);

    int selectCount(int userNo);

    int selectNewCount(int userNo);

    int insert(Alarm alarm);

    int updateList(int userNo);

    int update(Alarm alarm);

    int delete(Alarm alarm);

    int deleteAll(int userNo);
}
