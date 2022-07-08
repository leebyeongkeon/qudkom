package com.qudkom.web.dao.board.post.comment;

import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.comment.CommentLike;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentLikeDaoImpl implements CommentLikeDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.post.comment.CommentLikeMapper.";
    @Override
    public boolean selectExists(CommentLike commentLike) {
        return session.selectOne(namespace+"selectExists",commentLike);
    }
    @Override
    public int insert(CommentLike commentLike){
        return session.insert(namespace+"insert",commentLike);
    }
    @Override
    public int delete(CommentLike commentLike){
        return session.delete(namespace+"delete",commentLike);
    }

    @Override
    public CommentLike select(CommentLike commentLike) {
        return session.selectOne(namespace+"select", commentLike);
    }

    @Override
    public int deleteList(Comment comment) {
        return session.delete(namespace+"deleteList",comment);
    }
}
