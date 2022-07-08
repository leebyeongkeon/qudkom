package com.qudkom.web.domain.dto.param.bundle;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.Paging;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MemoParamBundle {
    public static final String RECEIVE_CATEGORY="receive";
    public static final String SEND_CATEGORY="send";
    public static final String TEMP_CATEGORY="temp";

    private MemoDto memoDto;
    private String category;
    private List<Integer> memoNoList;
    private Integer unreadCount;

    private Paging paging;

    @Builder
    public MemoParamBundle(MemoDto memoDto, String category, List<Integer> memoNoList, Paging paging, Integer unreadCount) {
        this.memoDto = memoDto;
        this.category = category;
        this.memoNoList = memoNoList;
        this.paging=paging;
        this.unreadCount = unreadCount;
    }
}
