package com.qudkom.web.domain.vo.board.post.comment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentLike {
    protected Integer boardNo;
    protected Integer postNo;
    protected Integer commentNo;
    protected Integer userNo;
    protected Boolean isLike;
    protected Boolean isVote;

    @Builder
    public CommentLike(Integer boardNo, Integer postNo, Integer commentNo, Integer userNo, Boolean isLike) {
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.commentNo = commentNo;
        this.userNo = userNo;
        this.isLike = isLike;
    }

//    public static CommentLike toCommentLikeEntity(CommentLikeDto commentLikeDto){
//        return CommentLike.builder()
//                .boardNo(commentLikeDto.getBoardNo())
//                .postNo(commentLikeDto.getPostNo())
//                .commentNo(commentLikeDto.getCommentNo())
//                .userNo(commentLikeDto.getUserNo())
//                .like(commentLikeDto.getLike())
//                .build();
//    }
//    public static Comment toCommentEntity(CommentLikeDto commentLikeDto){
//        return Comment.builder()
//                .boardNo(commentLikeDto.getBoardNo())
//                .postNo(commentLikeDto.getPostNo())
//                .commentNo(commentLikeDto.getCommentNo())
//                .build();
//    }
}
