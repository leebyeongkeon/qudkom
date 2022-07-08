
package com.qudkom.web.service.user.memo;

import com.qudkom.web.dao.user.memo.MemoDao;
import com.qudkom.web.dao.user.memo.MemoTemporaryDao;
import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.view.MemoViewDto;
import com.qudkom.web.domain.dto.view.handler.MemoPageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoServiceImpl implements MemoService {
    @Autowired
    MemoDao memoDao;
    @Autowired
    MemoTemporaryDao memoTemporaryDao;
    @Override
    public MemoViewDto getMemoView(MemoParamBundle memoParamBundle){
        MemoViewDto memoViewDto=new MemoViewDto();
        MemoDto memoDto= memoParamBundle.getMemoDto();
        int userNo=memoDto.getUserNo();
        Integer memoNo=memoDto.getMemoNo();
        String category= memoParamBundle.getCategory();
        List<MemoDto> memoDtoList=null;
        int totalCount=0;
        int unreadCount=0;
        if(category.equals(MemoParamBundle.RECEIVE_CATEGORY)) {
            if(memoNo!=null){
                //예외 날 경우 selectExists추가
                memoDao.updateOpened(memoDto);
                memoViewDto.setMemoDto(memoDao.selectReceive(memoDto));
            }
            memoDao.updateNewCount(userNo);
            memoDtoList=memoDao.selectReceiveList(memoParamBundle);
            totalCount=memoDao.selectReceiveCount(memoDto);
            unreadCount=memoDao.selectUnreadReceiveCount(memoDto);
        }
        else if(category.equals(MemoParamBundle.SEND_CATEGORY)){
            if(memoNo!=null){
                memoViewDto.setMemoDto(memoDao.selectSend(memoDto));
            }
            memoDtoList=memoDao.selectSendList(memoParamBundle);
            totalCount=memoDao.selectSendCount(memoDto);
            unreadCount=memoDao.selectUnreadSendCount(memoDto);
        }else if(category.equals(MemoParamBundle.TEMP_CATEGORY)){
            if(memoNo!=null){
                memoViewDto.setMemoDto(memoTemporaryDao.select(memoDto));
            }
            memoDtoList=memoTemporaryDao.selectList(memoParamBundle);
            totalCount=memoTemporaryDao.selectCount(userNo);
        }
        Paging paging= memoParamBundle.getPaging();
        MemoPageHandler memoPageHandler=new MemoPageHandler(
                totalCount,
            paging.getPage(),paging.getPageSize(),paging.getListSize()
//                memoParamDto.getPage(),
//                memoParamDto.getPageSize(),
//                memoParamDto.getListSize()
        );
        memoViewDto.setCategory(category);
        memoViewDto.setMemoList(memoDtoList);
        memoViewDto.setTotalCount(totalCount);
        memoViewDto.setUnreadCount(unreadCount);
        memoViewDto.setMemoPageHandler(memoPageHandler);
        return memoViewDto;
    }
    @Override
    public int sendMemo(MemoDto memoDto){
        return memoDao.insert(memoDto);
    }
    @Override
    public int saveTemp(MemoDto memoDto){
        return memoTemporaryDao.insert(memoDto);
    }
    @Override
    public int moveMemoList(MemoParamBundle memoParamBundle){
        int rowCnt=0;
        String category= memoParamBundle.getCategory();
        if(category.equals(MemoParamBundle.RECEIVE_CATEGORY)){
            rowCnt=memoDao.updateReceiveList(memoParamBundle);
        }else if(category.equals(MemoParamBundle.SEND_CATEGORY)){
            rowCnt=memoDao.updateSendList(memoParamBundle);
        }
        return rowCnt;
    }
    @Override
    public int eraseMemoList(MemoParamBundle memoParamBundle){
        int rowCnt=0;
        String category= memoParamBundle.getCategory();
        if(category.equals(MemoParamBundle.RECEIVE_CATEGORY)){
            rowCnt=memoDao.deleteReceiveList(memoParamBundle);
        }else if(category.equals(MemoParamBundle.SEND_CATEGORY)){
            rowCnt=memoDao.deleteSendList(memoParamBundle);
        }else if(category.equals(MemoParamBundle.TEMP_CATEGORY)){
            rowCnt=memoTemporaryDao.deleteList(memoParamBundle);
        }
        return rowCnt;
    }
    @Override
    public int getNewMemoCount(int userNo){
        return memoDao.selectNewCount(userNo);
    }
}
