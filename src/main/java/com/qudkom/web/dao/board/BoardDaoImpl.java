package com.qudkom.web.dao.board;

import com.qudkom.web.domain.dto.extended.board.BoardDto;
import com.qudkom.web.domain.vo.board.Board;
import com.qudkom.web.domain.vo.board.BoardCategory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.BoardMapper.";

    @Override
    public Board selectBoardByBoardNo(int boardNo){
        return session.selectOne(namespace+"select",boardNo);
    }//게시판 기본 정보
    @Override
    public List<Board> selectAccessibleBoardList(BoardCategory boardCategory){
        return session.selectList(namespace+"selectList", boardCategory);
    }//일반 유저가 접근할 수 있는 게시판 목록
    @Override
    public List<Board> selectBoardList(){
        return null;
    }

    @Override
    public int selectBoardCategoryNo(int boardNo) {
        return session.selectOne(namespace+"selectBoardCategoryNo",boardNo);
    }

    @Override
    public List<BoardDto> selectIndexBoards(int status) {
        return session.selectList(namespace+"selectIndexBoards",status);
    }
//
//    public Post getPostByPostNo(int postNo){
//        return null;
//    }
//
//    public List<Post> getPostList(int postNo, int page, String query, String param){
//        return null;
//    }
//    public Post uploadPost(){
//        return null;
//    }
//    public Post editPost(){
//        return null;
//    }
//    public int deletePost(){
//        return 0;
//    }
//    public int votePost(){
//        return 0;
//    }
//    public int getPostCount(){
//        return 0;
//    }
//    public int getPostCount(String query, String param){
//        return 0;
//    }
}
