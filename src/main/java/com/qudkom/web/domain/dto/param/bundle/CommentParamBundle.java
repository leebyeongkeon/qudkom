package com.qudkom.web.domain.dto.param.bundle;

import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import lombok.Builder;
import lombok.Data;

@Data
public class CommentParamBundle {
    private Comment comment;

    private Boolean isLike;
    private Boolean cancel;

    private Boolean usePage;
    private Boolean completelyErased;

    private Paging paging;

    private Integer commentParam;

    public CommentParamBundle(){
    this.completelyErased=false;
}

    @Builder
    public CommentParamBundle(Comment comment, Integer commentParam, Boolean isLike, Boolean cancel, Boolean usePage, Boolean completelyErased, Paging paging) {
        this.comment = comment;
        this.commentParam = commentParam;
        this.isLike = isLike;
        this.cancel = cancel;
        this.usePage = usePage;
        this.completelyErased = completelyErased;
        this.paging=paging;
    }
}