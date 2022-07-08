package com.qudkom.web.dao.board.post.comment;

import com.qudkom.web.domain.dto.extended.board.post.comment.CommentDto;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.param.bundle.CommentParamBundle;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.comment.CommentLike;

import java.util.List;

public interface CommentDao {
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
    List<Comment> selectList(CommentParamBundle commentParamBundle);

    List<Comment> makeCommentTree(Comment dummyRoot);

////////////////
    List<Integer> selectNoList(Comment comment);

    List<CommentDto> selectListCTE(CommentParamBundle commentParamBundle);

    Comment select(Comment comment);

    int selectCountByUserNo(int userNo);

    List<Comment> selectByUserNo(int userNo);

    Comment selectLikeCount(CommentLike commentLike);

    boolean selectExists(Comment comment);

    boolean selectExistsChild(Comment comment);

    int insert(Comment comment);

    int update(Comment comment);

    int updateCountField(BoardParamBundle boardParamBundle);

    int updateStatus(Comment comment);

    int delete(Comment comment);

    Comment selectParentComment(Comment comment);
}
