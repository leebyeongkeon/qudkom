package com.qudkom.web.dao.user.memo;

import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.vo.user.memo.Memo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemoDaoImpl implements MemoDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.user.memo.MemoMapper.";
    @Override
    public List<MemoDto> selectReceiveList(MemoParamBundle memoParamBundle){
        return session.selectList(namespace+"selectReceiveList", memoParamBundle);
    }
    @Override
    public List<MemoDto> selectSendList(MemoParamBundle memoParamBundle){
        return session.selectList(namespace+"selectSendList", memoParamBundle);
    }
    @Override
    public MemoDto selectReceive(Memo memo){
        return session.selectOne(namespace+"selectReceive",memo);
    }
    @Override
    public MemoDto selectSend(Memo memo){
        return session.selectOne(namespace+"selectSend",memo);
    }
    @Override
    public int selectUnreadReceiveCount(Memo memo){
        return session.selectOne(namespace+"selectUnreadReceiveCount",memo);
    }
    @Override
    public int selectUnreadSendCount(Memo memo){
        return session.selectOne(namespace+"selectUnreadSendCount",memo);
    }
    @Override
    public int selectReceiveCount(Memo memo){
        return session.selectOne(namespace+"selectReceiveCount",memo);
    }
    @Override
    public int selectSendCount(Memo memo){
        return session.selectOne(namespace+"selectSendCount",memo);
    }
    @Override
    public int selectNewCount(int userNo){
        return session.selectOne(namespace+"selectNewCount",userNo);
    }
    @Override
    public int insert(MemoDto memoDto){
        return session.insert(namespace+"insert",memoDto);
    }
    @Override
    public int updateNewCount(int userNo){
        return session.update(namespace+"updateNewCount",userNo);
    }
    @Override
    public int updateOpened(Memo memo){
        return session.update(namespace+"updateOpened",memo);
    }
    @Override
    public int updateReceiveList(MemoParamBundle memoParamBundle){
        return session.update(namespace+"updateReceiveList", memoParamBundle);
    }
    @Override
    public int updateSendList(MemoParamBundle memoParamBundle){
        return session.update(namespace+"updateSendList", memoParamBundle);
    }
    @Override
    public int deleteReceiveList(MemoParamBundle memoParamBundle){
        return session.update(namespace+"deleteReceiveList", memoParamBundle);
    }
    @Override
    public int deleteSendList(MemoParamBundle memoParamBundle){
        return session.update(namespace+"deleteSendList", memoParamBundle);
    }

//
//
//    public int updateSendSave(Memo memo){
//        return session.update(namespace+"updateSendSave",memo);
//    }
//    public int updateReceiveSave(Memo memo){
//        return session.update(namespace+"updateReceiveSave",memo);
//    }
//    public int updateSendDelete(Memo memo){
//        return session.update(namespace+"updateSendDelete",memo);
//    }
//    public int updateReceiveDelete(Memo memo){
//        return session.update(namespace+"updateReceiveDelete",memo);
//    }
//    public int updateReceiveConfirm(Memo memo){
//        return session.update(namespace+"updateReceiveConfirm",memo);
//    }
//    public int deleteList(MemoParamDto memoParamDto) {
//        return session.update(namespace + "deleteList", memoParamDto);
//    }
//    public int deleteAfterCheck(Memo memo){
//        return session.delete(namespace+"deleteAfterCheck",memo);
//    }
}
