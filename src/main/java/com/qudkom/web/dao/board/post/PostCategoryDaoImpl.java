package com.qudkom.web.dao.board.post;

import com.qudkom.web.domain.vo.board.post.PostCategory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostCategoryDaoImpl implements PostCategoryDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.post.PostCategoryMapper.";
    @Override
    public List<PostCategory> selectList(int boardCategoryNo){
        return session.selectList(namespace+"selectList",boardCategoryNo);
    }

    @Override
    public PostCategory select(PostCategory postCategory) {
        return session.selectOne(namespace+"select",postCategory);
    }
}
