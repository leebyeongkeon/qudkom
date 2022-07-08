package com.qudkom.web.service.board.post.comment;

import com.qudkom.web.domain.dto.extended.board.post.comment.CommentDto;
import com.qudkom.web.domain.dto.param.bundle.CommentParamBundle;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.vo.board.post.comment.CommentLike;
import com.qudkom.web.domain.dto.view.CommentViewDto;
import com.qudkom.web.exception.AlreadyVotedException;

import java.sql.SQLException;
import java.util.List;

public interface CommentService {
//    @Transactional
    @Deprecated
    CommentViewDto writeComment(Comment comment) throws SQLException;

    @Deprecated
    CommentViewDto getCommentList(CommentParamBundle commentParamBundle);

    @Deprecated
    CommentViewDto editComment(Comment comment) throws SQLException;

    @Deprecated
    CommentViewDto eraseComment(Comment comment) throws SQLException;

    int insertComment(Comment comment) throws SQLException;
//    @Transactional

    int deleteComment(Comment comment) throws SQLException;

//    @Transactional
    int updateCommentLike(CommentLike commentLike, String sign)throws SQLException;

    Comment voteComment(CommentLike commentLike) throws SQLException, AlreadyVotedException;

    Comment cancelVoteComment(CommentLike commentLike) throws SQLException;

    //기본 조회, 페이징, 번호로 조회, 수정, 삭제 시
    CommentViewDto writeCommentCTE(CommentParamBundle commentParamBundle) throws SQLException;

    CommentViewDto eraseCommentCTE(CommentParamBundle commentParamBundle) throws SQLException;

    CommentViewDto editCommentCTE(CommentParamBundle commentParamBundle) throws SQLException;

    CommentViewDto getCommentCTE(CommentParamBundle commentParamBundle);

    boolean checkExists(Comment comment);

    List<CommentDto> getCommentListByNo(CommentParamBundle commentParamBundle);

    int calcCommentPage(CommentParamBundle commentParamBundle);

//    List<CommentDto> getCommentListCTE(CommentParamDto commentParamDto);

//    List<CommentDto> getCommentListByNo(Comment Comment);
}
