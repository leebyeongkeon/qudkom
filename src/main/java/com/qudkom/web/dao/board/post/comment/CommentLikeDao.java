package com.qudkom.web.dao.board.post.comment;

import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.comment.CommentLike;

public interface CommentLikeDao {
    boolean selectExists(CommentLike commentLike);

    int insert(CommentLike commentLike);

    int delete(CommentLike commentLike);

    CommentLike select(CommentLike commentLike);

    int deleteList(Comment comment);
}
