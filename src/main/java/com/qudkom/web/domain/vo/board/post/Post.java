package com.qudkom.web.domain.vo.board.post;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ToString
@Data
public class Post {
    public static final String COMMENT_COUNT_FIELD_NAME="comment_count";
    public static final String LIKE_COUNT_FIELD_NAME="like_count";
    public static final String DISLIKE_COUNT_FIELD_NAME="dislike_count";
    public static final String HIT_FIELD_NAME="hit";

    protected Integer boardNo;
    protected Integer postNo;

    protected Integer userNo;
    protected String nickname;
    protected String profile;
    protected Integer exp;


    protected String title;
    protected String content;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date postDate;
    protected Integer hit;

    protected Integer likeCount;
    protected Integer dislikeCount;
    protected Integer commentCount;

    protected Boolean hasImage;
    protected Integer postCategoryNo;
    protected Integer status;//임시저장, 삭제, 블라인드

    public Post(){}
    @Builder
    public Post(Integer boardNo, Integer postNo, Integer userNo, String nickname, String profile, Integer exp, String title, String content, Date postDate, Integer hit, Integer likeCount, Integer dislikeCount, Integer commentCount, Boolean hasImage, Integer postCategoryNo, Integer status) {
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.userNo = userNo;
        this.nickname = nickname;
        this.profile = profile;
        this.exp = exp;
        this.title = title;
        this.content = content;
        this.postDate = postDate;
        this.hit = hit;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.hasImage=hasImage;
        this.postCategoryNo = postCategoryNo;
        this.status = status;
    }


    //    public static Post toPostEntity(PostDto postDto){
//        return Post.builder()
//                .boardNo(postDto.getBoardNo())
//                .postNo(postDto.getPostNo())
//                .userNo(postDto.getUserNo())
//                .commentCount(postDto.getCommentCount())
//                .content(postDto.getContent())
//                .dislikeCount(postDto.getDislikeCount())
//                .likeCount(postDto.getLikeCount())
//                .postDate(postDto.getPostDate())
//                .hit(postDto.getHit())
//                .status(postDto.getStatus())
//                .categoryNo(postDto.getCategoryNo())
//                .title(postDto.getTitle())
//                .nickname(postDto.getNickname())
//                .profile(postDto.getProfile())
//                .exp(postDto.getExp())
//                .build();
//    }

}
