package com.qudkom.web.dao.user.memo;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.vo.user.memo.MemoTemporary;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemoTemporaryDaoImpl implements MemoTemporaryDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.user.memo.MemoTemporaryMapper.";
    @Override
    public List<MemoDto> selectList(MemoParamBundle memoParamBundle){
        return session.selectList(namespace+"selectList", memoParamBundle);
    }
    @Override
    public MemoDto select(MemoDto memoDto){
        return session.selectOne(namespace+"select",memoDto);
    }
    @Override
    public int selectCount(int userNo){
        return session.selectOne(namespace+"selectCount", userNo);
    }

    @Override
    public int insert(MemoDto memoDto){
        return session.insert(namespace+"insert", memoDto);
    }
    @Override
    public int update(MemoTemporary memoTemporary){
        return session.update(namespace+"update",memoTemporary);
    }
    @Override
    public int deleteList(MemoParamBundle memoParamBundle){
        return session.delete(namespace+"deleteList", memoParamBundle);
    }
//    public int delete(MemoTemporary memoTemporary){
//        return session.delete(namespace+"delete",memoTemporary);
//    }
}
