package com.qudkom.web.dao.user.memo;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.vo.user.memo.MemoTemporary;

import java.util.List;

public interface MemoTemporaryDao {
    List<MemoDto> selectList(MemoParamBundle memoParamBundle);

    MemoDto select(MemoDto memoDto);

    int selectCount(int userNo);

    int insert(MemoDto memoDto);

    int update(MemoTemporary memoTemporary);

    int deleteList(MemoParamBundle memoParamBundle);
}
