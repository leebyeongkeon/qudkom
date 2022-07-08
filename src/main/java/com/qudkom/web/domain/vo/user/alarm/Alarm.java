package com.qudkom.web.domain.vo.user.alarm;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class Alarm {
    protected Integer userNo;
    protected Integer alarmNo;
    protected Date alarmDate;
    protected Integer boardNo;
    protected Integer postNo;
    protected Integer commentNo;
    protected Integer status;

    public Alarm(){}

    @Builder
    public Alarm(Integer userNo, Integer alarmNo, Date alarmDate, Integer boardNo, Integer postNo, Integer commentNo, Integer status) {
        this.userNo = userNo;
        this.alarmNo = alarmNo;
        this.alarmDate = alarmDate;
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.commentNo = commentNo;
        this.status = status;
    }
}
