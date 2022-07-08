package com.qudkom.web.service.user.memo;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.dto.view.MemoViewDto;

public interface MemoService {
    MemoViewDto getMemoView(MemoParamBundle memoParamBundle);

    int sendMemo(MemoDto memoDto);

    int saveTemp(MemoDto MemoDto);

    int moveMemoList(MemoParamBundle memoParamBundle);

    int eraseMemoList(MemoParamBundle memoParamBundle);

    int getNewMemoCount(int userNo);
}
