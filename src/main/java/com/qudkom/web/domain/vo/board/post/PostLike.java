package com.qudkom.web.domain.vo.board.post;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Data
public class PostLike {
    protected Integer boardNo;
    protected Integer postNo;
    protected Integer userNo;
    protected Boolean isLike;

    @Builder
    public PostLike(Integer boardNo, Integer postNo, Integer userNo, Boolean isLike) {
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.userNo = userNo;
        this.isLike = isLike;
    }

//    public static PostLike toPostLikeEntity(PostLikeDto postLikeDto){
//        return PostLike.builder()
//                .boardNo(postLikeDto.getBoardNo())
//                .postNo(postLikeDto.getPostNo())
//                .userNo(postLikeDto.getUserNo())
//                .like(postLikeDto.getLike())
//                .build();
//    }
//
//    public static Post toPostEntity(PostLikeDto postLikeDto){
//        return Post.builder()
//                .boardNo(postLikeDto.getBoardNo())
//                .postNo(postLikeDto.getPostNo())
//                .build();
//    }
}
