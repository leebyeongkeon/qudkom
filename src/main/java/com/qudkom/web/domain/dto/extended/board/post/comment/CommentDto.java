package com.qudkom.web.domain.dto.extended.board.post.comment;

import com.qudkom.web.domain.vo.board.post.comment.Comment;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//@Data
public class CommentDto extends Comment {
    @Getter@Setter
    private String parentCommentNickname;

    public CommentDto(){

    }

//    public void setParentCommentNickname(String parentCommentNickname) {
//        System.out.println("setter parent name");
//        this.parentCommentNickname = parentCommentNickname;
//    }

    //    @Builder
    public CommentDto(Integer boardNo, Integer postNo, Integer commentNo, Integer userNo, String nickname, String profile, Integer exp, Date commentDate, String content, Integer likeCount, Integer dislikeCount, Integer parentComment, Integer status, Integer depth, String parentCommentNickname) {
        super(boardNo, postNo, commentNo, userNo, nickname, profile, exp, commentDate, content, likeCount, dislikeCount, parentComment, status, depth);
        this.parentCommentNickname = parentCommentNickname;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "parentCommentNickname='" + parentCommentNickname + '\'' +
                ", boardNo=" + boardNo +
                ", postNo=" + postNo +
                ", commentNo=" + commentNo +
                ", userNo=" + userNo +
                ", nickname='" + nickname + '\'' +
                ", profile='" + profile + '\'' +
                ", exp=" + exp +
                ", commentDate=" + commentDate +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                ", dislikeCount=" + dislikeCount +
                ", parentComment=" + parentComment +
                ", status=" + status +
                ", depth=" + depth +
                ", children=" + children +
                '}';
    }
}
