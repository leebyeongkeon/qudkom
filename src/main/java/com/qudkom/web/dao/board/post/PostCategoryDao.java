package com.qudkom.web.dao.board.post;

import com.qudkom.web.domain.vo.board.post.PostCategory;

import java.util.List;

public interface PostCategoryDao {
    List<PostCategory> selectList(int boardCategoryNo);

    PostCategory select(PostCategory postCategory);
}
