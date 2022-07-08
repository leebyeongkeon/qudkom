package com.qudkom.web.dao.board.post.comment;

import com.qudkom.web.domain.dto.extended.board.post.comment.CommentDto;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.param.bundle.CommentParamBundle;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.comment.CommentLike;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class CommentDaoImpl implements CommentDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.post.comment.CommentMapper.";
    /*
    댓글 번호로 리스트 가져오기
    */
//    public List<Comment> selectCommentListByCommentNo(int boardNo, int postNo, int commentNo){
//    }
    /*서비스에서 호출할 함수
    댓글 페이지로 리스트 가져오기
    0) 게시판 번호, 게시글 번호, 댓글 매개변수로 public 함수를 호출하면,
    1) 게시판 번호, 게시글 번호로 해당 게시물의 전체 댓글 목록을 트리맵 형태로 가져온 후,
    2) 깊이 우선 탐색으로 트리 구조를 순회하면서 리스트에 추가하여 정렬하고,
        +) 2.5) 댓글 파라미터가 댓글 번호인 경우 해당 댓글이 몇 페이지에 속하는지 계산하여
    3) 해당 댓글 페이지에 속하는 댓글들만을 다시 새로운 리스트에 담아서 추출하여 반환
    */
    @Override
    public List<Comment> selectList(CommentParamBundle commentParamBundle){
        Comment dummyRoot=selectUnsortedCommentList(commentParamBundle);
        List<Comment> sortedCommentList=makeCommentTree(dummyRoot);
        //정렬해서 가져오는 것까지만 여기서 하고,
        //페이지에 따른 추출 구현은 PageHandler에서 하고 흐름을 서비스에서 
        //댓글 번호로 페이지를 알아내서 추출하는 것도 PageHandler에서 구현하고 서비스에서 흐름 제어하기
        return sortedCommentList;
    }
    @Deprecated
    private Comment selectUnsortedCommentList(CommentParamBundle commentParamBundle){

//        Comment dummyRoot=new Comment();
        Comment dummyRoot= commentParamBundle.getComment();
        dummyRoot.setCommentNo(0);dummyRoot.setDepth(0);
        Map<Integer, Comment> map=new HashMap<>();
        map.put(0,dummyRoot);
//        int boardNo=commentParamDto.getComment().getBoardNo(), postNo=commentParamDto.getPostNo();
        int boardNo=dummyRoot.getBoardNo(), postNo=dummyRoot.getPostNo();

        String sql="SELECT * FROM comment WHERE board_no=? AND post_no=?";
        try(Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/qudkom",
                "qudk","1234");
            PreparedStatement pstmt=conn.prepareStatement(sql)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            pstmt.setInt(1, boardNo);
            pstmt.setInt(2, postNo);
            try(ResultSet rs=pstmt.executeQuery()) {
                while (rs.next()) {
//                    Comment comment=new Comment(
//                            rs.getInt("board_no"),
//                            rs.getInt("post_no"),
//                            rs.getInt("comment_no"),
//                            rs.getInt("user_no"),
//                            rs.getString("nickname"),
//                            rs.getString("profile"),
//                            rs.getInt("exp"),
//                            rs.getTimestamp("comment_date"),
//                            rs.getString("content"),
//                            rs.getInt("parent_comment"),
//                            rs.getInt("like_count"),
//                            rs.getInt("dislike_count"),
//                            rs.getInt("status"),
//                            rs.getInt("depth")
//                    );
                    Comment comment=Comment.builder()
                            .boardNo(rs.getInt("board_no"))
                            .postNo(rs.getInt("post_no"))
                            .commentNo(rs.getInt("comment_no"))
                            .userNo(rs.getInt("user_no"))
                            .nickname(rs.getString("nickname"))
                            .profile(rs.getString("profile"))
                            .exp(rs.getInt("exp"))
                            .commentDate(rs.getTimestamp("comment_date"))
                            .content(rs.getString("content"))
                            .parentComment(rs.getInt("parent_comment"))
                            .likeCount(rs.getInt("like_count"))
                            .dislikeCount(rs.getInt("dislike_count"))
                            .status(rs.getInt("status"))
                            .depth(rs.getInt("depth"))
                            .build();

                    map.put(comment.getCommentNo(), comment);
                    //시간x<-수정시간일수도 있으므로,
                    // 댓글번호 asc로 가져올 경우 부모 댓글의 번호가 맵에 항상 먼저 들어있을 것이다 (0 포함)
                    map.get(comment.getParentComment()).getChildren().add(comment);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            new SQLException();
        }
        return dummyRoot;
    }
    @Deprecated
    @Override
    public List<Comment> makeCommentTree(Comment dummyRoot){
        List<Comment> commentList=new ArrayList<>();
        Stack<Comment> stack=new Stack<>();
        //더미 댓글을 list에 추가할 경우 list를 잘라낼때 꼬일 것이다
        //depth가 1인 것들만 list로 가져와서 넣던지, 번호가 0인 dummy댓글을 만들어서 시작하기
        for(Comment comment: dummyRoot.getChildren()) {//depth가 1인 것들 뒷번호부터
            stack.push(comment);
            while (!stack.isEmpty()) {
                Comment poped = stack.pop();
                commentList.add(poped);
                for(int i=poped.getChildren().size()-1;i>=0;i--){
                    stack.push(poped.getChildren().get(i));
                }
            }
        }
        return commentList;
    }

////////////////
    @Override
    public List<Integer> selectNoList(Comment comment){
        return session.selectList(namespace+"selectNoList", comment);
    }
    @Override
    public List<CommentDto> selectListCTE(CommentParamBundle CommentParamBundle){
        return session.selectList(namespace+"selectList", CommentParamBundle);
    }
///////////////


    @Override
    public Comment select(Comment comment) {
        return session.selectOne(namespace+"select",comment);
    }

    @Override
    public int selectCountByUserNo(int userNo){
        return session.selectOne(namespace+"selectCountByUserNo",userNo);
    }

    @Override
    public List<Comment> selectByUserNo(int userNo){
        return session.selectList(namespace+"selectByUserNo",userNo);
    }

    @Override
    public Comment selectLikeCount(CommentLike commentLike){
        return session.selectOne(namespace+"selectLikeCount", commentLike);
    }

    @Override
    public boolean selectExists(Comment comment) {
        return session.selectOne(namespace+"selectExists",comment);
    }

    @Override
    public boolean selectExistsChild(Comment comment) {
        return session.selectOne(namespace+"selectExistsChild",comment);
    }

    @Override
    public int insert(Comment comment){
        return session.insert(namespace+"insert",comment);//댓글번호 담아서 쓸 수 있도록
    }

    @Override
    public int update(Comment comment){
        System.out.println("comment 댓글 수정 dao = " + comment);
        return session.update(namespace+"update",comment);
    }

    @Override
    public int updateCountField(BoardParamBundle boardParamBundle){// 부호는 - or +
        return session.update(namespace+"updateCountField", boardParamBundle);
    }

    @Override
    public int updateStatus(Comment comment) {
        return session.update(namespace+"updateStatus",comment);
    }

    @Override
    public int delete(Comment comment){
        return session.delete(namespace+"delete",comment);
    }

    @Override
    public Comment selectParentComment(Comment comment) {
        return session.selectOne(namespace+"selectParentComment",comment);
    }
}
