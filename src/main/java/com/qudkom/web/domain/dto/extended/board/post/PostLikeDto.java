package com.qudkom.web.domain.dto.extended.board.post;

import com.qudkom.web.domain.vo.board.post.PostLike;
import lombok.*;

//@ToString
@NoArgsConstructor
@Data
public class PostLikeDto extends PostLike {
    private Integer writerUserNo;

//    @Builder
    public PostLikeDto(Integer boardNo, Integer postNo, Integer userNo, Boolean isLike, Integer writerUserNo) {
        super(boardNo, postNo, userNo, isLike);
        this.writerUserNo = writerUserNo;
    }

    @Override
    public String toString() {
        return "PostLikeDto{" +
                "postUserNo=" + writerUserNo +
                ", boardNo=" + boardNo +
                ", postNo=" + postNo +
                ", userNo=" + userNo +
                ", isLike=" + isLike +
                '}';
    }
}
