package com.qudkom.web.dao.board.post;

import com.qudkom.web.domain.dto.extended.board.post.PostDto;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.vo.board.Board;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.comment.Comment;

import java.util.List;

public interface PostDao {
    PostDto select(Post post);

    int selectCount(int boardNo);

    List<PostDto> selectList(BoardParamBundle boardParamBundle);

    int selectCountByUserNo(int userNo);

    int selectCommentCount(Comment comment);

    List<Post> selectListByUserNo(BoardParamBundle boardParamBundle);

    int selectCountByUserNoInBoard(BoardParamBundle boardParamBundle);

    List<Post> selectListByUserNoInBoard(BoardParamBundle boardParamBundle);

    int selectSearchedCountAll(BoardParamBundle boardParamBundle);

    List<PostDto> selectSearchedListAll(BoardParamBundle boardParamBundle);

    int selectSearchedCountInBoard(BoardParamBundle boardParamBundle);

    List<PostDto> selectSearchedListInBoard(BoardParamBundle boardParamBundle);

    int insert(Post post);

    int update(Post post)//좋아요 싫어요수, 댓글수 업데이트 포함
    ;

//    int selectCommentCount(Post post);

    int updateCountFieldInPost(BoardParamBundle boardParamBundle);

    int delete(Post post);
}
