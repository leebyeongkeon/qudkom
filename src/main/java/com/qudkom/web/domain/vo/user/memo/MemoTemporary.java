package com.qudkom.web.domain.vo.user.memo;

import lombok.Data;

import java.util.Date;

@Data
public class MemoTemporary {
    protected Integer sendUser;
    protected Integer tempNo;
    protected Integer receiveUser;
    protected String title;
    protected String content;
    protected Date memoDate;
}
