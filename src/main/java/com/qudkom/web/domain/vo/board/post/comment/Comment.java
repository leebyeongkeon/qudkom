package com.qudkom.web.domain.vo.board.post.comment;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
@Data
public class Comment {
    public static final String LIKE_COUNT_FIELD_NAME="like_count";
    public static final String DISLIKE_COUNT_FIELD_NAME="dislike_count";

    protected Integer boardNo;
    protected Integer postNo;
    protected Integer commentNo;

    protected Integer userNo;
    protected String nickname;
    protected String profile;
    protected Integer exp;

    protected Date commentDate;
    protected String content;
    protected Integer likeCount;
    protected Integer dislikeCount;

    protected Integer parentComment;
    protected Integer status;
    protected Integer depth;

    protected List<Comment> children=new ArrayList<>();
//    private PriorityQueue<CommentDto> children;
//    private Comparator comparator=new Comparator() {
//        @Override
//        public int compare(Object o1, Object o2) {
//            return 0;
//        }
//    }
    public Comment(){
//        this.children=new ArrayList<>();
//        this.children=new PriorityQueue<>();
    }
    @Builder
    public Comment(Integer boardNo, Integer postNo, Integer commentNo, Integer userNo, String nickname, String profile, Integer exp, Date commentDate, String content, Integer likeCount, Integer dislikeCount, Integer parentComment, Integer status, Integer depth) {
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.commentNo = commentNo;
        this.userNo = userNo;
        this.nickname = nickname;
        this.profile = profile;
        this.exp = exp;
        this.commentDate = commentDate;
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.parentComment = parentComment;
        this.status = status;

        this.depth = depth;
//        this.children=new ArrayList<>();
//        this.children=new PriorityQueue<>();
    }

//    public void setDepth(Integer depth) {
//        if(depth>3)
//            depth=3;
//        this.depth = depth;
//    }

    //    public static Comment toCommentEntity(CommentDto commentDto) {
//        return Comment.builder()
//                .boardNo(commentDto.getCommentNo())
//                .postNo(commentDto.getPostNo())
//                .commentNo(commentDto.getCommentNo())
//                .userNo(commentDto.getUserNo())
//                .nickname(commentDto.getNickname())
//                .content(commentDto.getContent())
//                .commentDate(commentDto.getCommentDate())
//                .likeCount(commentDto.getLikeCount())
//                .dislikeCount(commentDto.getDislikeCount())
//                .parentComment(commentDto.getParentComment())
//                .status(commentDto.getStatus())
//                .depth(commentDto.getDepth())
//                .build();
//    }

}
