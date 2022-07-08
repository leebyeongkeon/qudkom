package com.qudkom.web.domain.dto.extended.board.post.comment;

import com.qudkom.web.domain.vo.board.post.comment.CommentLike;
import lombok.*;

@NoArgsConstructor
@Data
public class CommentLikeDto extends CommentLike {
    private Integer writerUserNo;

//    @Builder
    public CommentLikeDto(Integer boardNo, Integer postNo, Integer commentNo, Integer userNo, Boolean isLike, Integer writerUserNo) {
        super(boardNo, postNo, commentNo, userNo, isLike);
        this.writerUserNo = writerUserNo;
    }
}
