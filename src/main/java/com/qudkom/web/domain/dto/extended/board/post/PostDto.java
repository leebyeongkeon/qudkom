package com.qudkom.web.domain.dto.extended.board.post;

import com.qudkom.web.domain.vo.board.post.Post;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class PostDto extends Post {
    private String boardName;
    private String categoryName;
    public PostDto(){}
    public PostDto(Post post, String categoryName) {
        this.boardNo=post.getBoardNo();
        this.postNo=post.getPostNo();
        this.userNo=post.getUserNo();
        this.nickname= post.getNickname();
        this.profile=post.getProfile();
        this.exp=post.getExp();
        this.title=post.getTitle();
        this.content=post.getContent();
        this.postDate=post.getPostDate();
        this.hit=post.getHit();
        this.likeCount=post.getLikeCount();
        this.dislikeCount=post.getDislikeCount();
        this.commentCount=post.getCommentCount();
        this.hasImage=post.getHasImage();
        this.postCategoryNo=post.getPostCategoryNo();
        this.status=post.getStatus();
        this.categoryName=categoryName;
    }
//
//    public PostDto(Integer boardNo, Integer postNo, Integer userNo, String nickname, String profile, Integer exp, String title, String content, Date postDate, Integer hit, Integer likeCount, Integer dislikeCount, Integer commentCount, Boolean hasImage, Integer postCategoryNo, Integer status, String categoryName) {
//        super(boardNo, postNo, userNo, nickname, profile, exp, title, content, postDate, hit, likeCount, dislikeCount, commentCount, hasImage, postCategoryNo, status);
//        this.categoryName = categoryName;
//    }

    @Override
    public String toString() {
        return "PostDto{" +
                "boardName='" + boardName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", boardNo=" + boardNo +
                ", postNo=" + postNo +
                ", userNo=" + userNo +
                ", nickname='" + nickname + '\'' +
                ", profile='" + profile + '\'' +
                ", exp=" + exp +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", postDate=" + postDate +
                ", hit=" + hit +
                ", likeCount=" + likeCount +
                ", dislikeCount=" + dislikeCount +
                ", commentCount=" + commentCount +
                ", hasImage=" + hasImage +
                ", postCategoryNo=" + postCategoryNo +
                ", status=" + status +
                '}';
    }
}
