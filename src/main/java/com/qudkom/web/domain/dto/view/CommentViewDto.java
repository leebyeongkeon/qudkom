package com.qudkom.web.domain.dto.view;

import com.qudkom.web.domain.dto.extended.board.post.comment.CommentDto;
import com.qudkom.web.domain.dto.util.AlarmMessage;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import lombok.Data;

import java.util.List;

@Data
public class CommentViewDto {
    private List<Comment> commentList;
    private CommentPageHandler commentPageHandler;
    private Integer newCommentNo;
    private boolean completelyErased;

//    private AlarmMessage alarmMessage;
    private Integer alarmTargetUser;

    private List<CommentDto> commentDtoList;

    public CommentViewDto(){
        this.completelyErased=false;
        this.alarmTargetUser=0;
    }
}
