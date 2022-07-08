package com.qudkom.web.dao.user.memo;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.vo.user.memo.Memo;

import java.util.List;

public interface MemoDao {
    List<MemoDto> selectReceiveList(MemoParamBundle memoParamBundle);

    List<MemoDto> selectSendList(MemoParamBundle memoParamBundle);

    MemoDto selectReceive(Memo memo);

    MemoDto selectSend(Memo memo);

    int selectUnreadReceiveCount(Memo memo);

    int selectUnreadSendCount(Memo memo);

    int selectReceiveCount(Memo memo);

    int selectSendCount(Memo memo);

    int selectNewCount(int userNo);

    int insert(MemoDto memoDto);

    int updateNewCount(int userNo);

    int updateOpened(Memo memo);

    int updateReceiveList(MemoParamBundle memoParamBundle);

    int updateSendList(MemoParamBundle memoParamBundle);

    int deleteReceiveList(MemoParamBundle memoParamBundle);

    int deleteSendList(MemoParamBundle memoParamBundle);
}
