package com.qudkom.web.service.user.alarm;

import com.qudkom.web.domain.dto.param.bundle.AlarmParamBundle;
import com.qudkom.web.domain.dto.view.AlarmViewDto;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.user.alarm.Alarm;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

public interface AlarmService {
    AlarmViewDto getAlarmView(AlarmParamBundle alarmParamBundle);

    @Transactional(rollbackFor = Exception.class)
    int addAlarm(Comment newComment) throws SQLException;

    int getNewAlarmCount(int userNo);

    int eraseAlarm(Alarm alarm);
}
