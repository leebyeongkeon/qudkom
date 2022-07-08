package com.qudkom.web.dao.board.post;

import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.PostLike;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class   PostLikeDaoImpl implements PostLikeDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.post.PostLikeMapper.";

    @Override
    public boolean selectExists(PostLike postLike){
        return session.selectOne(namespace+"selectExists", postLike);
    }

    @Override
    public int insert(PostLike postLike){
        System.out.println("session = " + session);
        return session.insert(namespace+"insert", postLike);
    }

    @Override
    public int delete(PostLike postLike){
        return session.delete(namespace+"delete", postLike);
    }

    @Override
    public PostLike select(PostLike postLike) {
        return session.selectOne(namespace+"select",postLike);
    }

    @Override
    public int deleteList(Post post) {
        return session.delete(namespace+"deleteList",post);
    }
}
