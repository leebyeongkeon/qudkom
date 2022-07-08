package com.qudkom.web.domain.vo.user.memo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class Memo {
    protected Integer sendUser;
    protected Integer sendNo;
    protected Integer receiveUser;
    protected Integer receiveNo;
    protected String title;
    protected String content;
    protected Date memoDate;
    protected Boolean opened;
    protected Boolean sendSave;
    protected Boolean sendDelete;
    protected Boolean receiveSave;
    protected Boolean receiveDelete;
    protected Boolean receiveConfirm;
    public Memo(){}
    @Builder
    public Memo(Integer sendUser, Integer sendNo, Integer receiveUser, Integer receiveNo, String title, String content, Date memoDate, Boolean opened, Boolean sendSave, Boolean sendDelete, Boolean receiveSave, Boolean receiveDelete, Boolean receiveConfirm) {
        this.sendUser = sendUser;
        this.sendNo = sendNo;
        this.receiveUser = receiveUser;
        this.receiveNo = receiveNo;
        this.title = title;
        this.content = content;
        this.memoDate = memoDate;
        this.opened = opened;
        this.sendSave = sendSave;
        this.sendDelete = sendDelete;
        this.receiveSave = receiveSave;
        this.receiveDelete = receiveDelete;
        this.receiveConfirm = receiveConfirm;
    }
}
