package com.qudkom.web.domain.dto.view;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.view.handler.MemoPageHandler;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.domain.vo.user.memo.Memo;
import lombok.Data;

import java.util.List;

@Data
public class MemoViewDto {
    private String category;
    private Boolean save;
    private Integer totalCount;
    private Integer unreadCount;
    private MemoDto memoDto;
    private List<? extends Memo> memoList;
    private List<User> userList;
    private MemoPageHandler memoPageHandler;
}
