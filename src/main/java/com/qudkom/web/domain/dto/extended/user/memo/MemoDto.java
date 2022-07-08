package com.qudkom.web.domain.dto.extended.user.memo;

import com.qudkom.web.domain.vo.user.memo.Memo;
import lombok.Data;

@Data
public class MemoDto extends Memo {
    private Integer userNo;
    private Integer memoNo;
    private Boolean save;
    private Boolean delete;
    private Integer counterpartUser;
    private String nickname;
}
