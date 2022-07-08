package com.qudkom.web.service.user.alarm;

import com.qudkom.web.dao.board.post.PostDao;
import com.qudkom.web.dao.board.post.comment.CommentDao;
import com.qudkom.web.dao.user.alarm.AlarmDao;
import com.qudkom.web.domain.dto.extended.user.alarm.AlarmDto;
import com.qudkom.web.domain.dto.param.bundle.AlarmParamBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.view.AlarmViewDto;
import com.qudkom.web.domain.dto.view.handler.AlarmPageHandler;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.user.alarm.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    AlarmDao alarmDao;
    @Autowired
    PostDao postDao;
    @Autowired
    CommentDao commentDao;

    @Override
    public AlarmViewDto getAlarmView(AlarmParamBundle alarmParamBundle){
        AlarmViewDto alarmViewDto=new AlarmViewDto();
        List<AlarmDto> list = alarmDao.selectList(alarmParamBundle);
        Alarm alarm= alarmParamBundle.getAlarm();
        int totalCount=alarmDao.selectCount(alarm.getUserNo());
        Paging paging= alarmParamBundle.getPaging();
        AlarmPageHandler alarmPageHandler=new AlarmPageHandler(totalCount,paging.getPage(),
                paging.getPageSize(),paging.getListSize());
        alarmDao.updateList(alarm.getUserNo());
        alarmViewDto.setAlarmList(list);
        alarmViewDto.setAlarmPageHandler(alarmPageHandler);
        return alarmViewDto;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addAlarm(Comment newComment) throws SQLException {
        int targetUser=0;
        Post post=Post.builder()
                .boardNo(newComment.getBoardNo())
                .postNo(newComment.getPostNo())
                .build();
        post=postDao.select(post);
        Comment parentComment=Comment.builder().boardNo(newComment.getBoardNo())
                .postNo(newComment.getPostNo()).commentNo(newComment.getParentComment()).build();
        parentComment=commentDao.select(parentComment);

        boolean postUserAlarm=false;
        boolean parentCommentUserAlarm=false;
        if(post.getUserNo()!=newComment.getUserNo()){
            postUserAlarm=true;
        }
        if(newComment.getParentComment()!=null && newComment.getParentComment()!=0 &&
                newComment.getUserNo()!=parentComment.getUserNo() && !postUserAlarm){
            parentCommentUserAlarm=true;
        }
        int rowCnt=0;
        if(postUserAlarm){
            targetUser=post.getUserNo();
        }if(parentCommentUserAlarm){
            targetUser=parentComment.getUserNo();
        }
//        AlarmMessage alarmMessage=null;
        if(postUserAlarm || parentCommentUserAlarm){
            Alarm alarm=Alarm.builder()
                    .userNo(targetUser)
                    .boardNo(newComment.getBoardNo())
                    .postNo(newComment.getPostNo())
                    .commentNo(newComment.getCommentNo())
                    .build();
            rowCnt=alarmDao.insert(alarm);
            if(rowCnt==0)
                throw new SQLException();

//            alarmMessage=new AlarmMessage();
//            alarmMessage.setUserNo(targetUser);
//            alarmMessage.setType("alarm");
        }
        return targetUser;
    }
    @Override
    public int getNewAlarmCount(int userNo){
        return alarmDao.selectNewCount(userNo);
    }

    @Override
    public int eraseAlarm(Alarm alarm) {
        return alarmDao.delete(alarm);
    }
}
