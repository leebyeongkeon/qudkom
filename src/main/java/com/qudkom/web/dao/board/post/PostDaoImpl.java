package com.qudkom.web.dao.board.post;

import com.qudkom.web.domain.dto.extended.board.post.PostDto;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {
    @Autowired
    private SqlSession session;
    private static final String namespace = "com.qudkom.web.dao.board.post.PostMapper.";

    @Override
    public PostDto select(Post post) { //boardNo, postNo
        return session.selectOne(namespace + "select", post);
    }

    @Override
    public int selectCount(int boardNo) {
        return session.selectOne(namespace + "selectCount", boardNo);
    }

    @Override
    public List<PostDto> selectList(BoardParamBundle boardParamBundle) {
        return session.selectList(namespace + "selectList", boardParamBundle);
    }

    @Override
    public int selectCountByUserNo(int userNo) {
        return session.selectOne(namespace + "selectCountByUserNo", userNo);
    }

    @Override
    public int selectCommentCount(Comment comment) {
        return session.selectOne(namespace+"selectCommentCount",comment);
    }

    @Override
    public List<Post> selectListByUserNo(BoardParamBundle boardParamBundle) {
        return session.selectList(namespace + "selectListByUserNo", boardParamBundle);
    }

    @Override
    public int selectCountByUserNoInBoard(BoardParamBundle boardParamBundle) {
        return session.selectOne(namespace + "selectCountByUserNoInBoard", boardParamBundle);
    }

    @Override
    public List<Post> selectListByUserNoInBoard(BoardParamBundle boardParamBundle) {
        return session.selectList(namespace + "selectListByUserNoInBoard", boardParamBundle);
    }

    @Override//사이트 전체 검색 결과 수
    public int selectSearchedCountAll(BoardParamBundle boardParamBundle) {
        return session.selectOne(namespace + "selectSearchedCountAll", boardParamBundle);
    }//게시판 내 검색

    @Override//사이트 전체 검색 결과
    public List<PostDto> selectSearchedListAll(BoardParamBundle boardParamBundle) {
        return session.selectList(namespace + "selectSearchedListAll", boardParamBundle);
    }

    @Override//게시판 내 검색 결과 수
    public int selectSearchedCountInBoard(BoardParamBundle boardParamBundle) {
        return session.selectOne(namespace + "selectSearchedCountInBoard", boardParamBundle);
    }//사이트 전체 검색

    @Override//게시파 내 검색 결과
    public List<PostDto> selectSearchedListInBoard(BoardParamBundle boardParamBundle) {
        return session.selectList(namespace + "selectSearchedListInBoard", boardParamBundle);
    }

    @Override
    public int insert(Post post) {
        int rowCnt = session.insert(namespace + "insert", post);
        return rowCnt;
    }

    @Override
    public int update(Post post) {
        return session.update(namespace + "update", post);
    }//좋아요 싫어요수, 댓글수 업데이트 포함

    @Override
    public int updateCountFieldInPost(BoardParamBundle boardParamBundle){
        return session.update(namespace + "updateCountFieldInPost", boardParamBundle);
    }

    @Override
    public int delete(Post post) {
        return session.delete(namespace + "delete", post);
    }
}