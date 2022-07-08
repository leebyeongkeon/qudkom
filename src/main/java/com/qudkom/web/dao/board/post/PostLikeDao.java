package com.qudkom.web.dao.board.post;

import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.PostLike;

public interface PostLikeDao {
    boolean selectExists(PostLike postLike);

    int insert(PostLike postLike);

    int delete(PostLike postLike);

    PostLike select(PostLike postLike);

    int deleteList(Post post);
}
