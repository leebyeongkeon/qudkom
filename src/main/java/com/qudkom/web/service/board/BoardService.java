package com.qudkom.web.service.board;

import com.qudkom.web.domain.dto.extended.board.BoardDto;
import com.qudkom.web.domain.dto.extended.board.post.PostDto;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.view.MenuDto;
import com.qudkom.web.domain.dto.view.BoardViewDto;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.PostCategory;
import com.qudkom.web.domain.vo.board.post.PostLike;
import com.qudkom.web.exception.AlreadyVotedException;
import javassist.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface BoardService {

    List<MenuDto> getBoardMenu(int status);

    BoardDto getBoardDto(int boardNo) throws NotFoundException;

    BoardViewDto getBoardView(BoardParamBundle boardParamBundle) throws NotFoundException, NotFoundException;

    List<PostDto> getPostList(BoardParamBundle boardParamBundle);

    Post getPost(Post post);

    List<PostCategory> getPostCategoryList(int boardNo);

    int uploadPost(Post post) throws SQLException;

    int editPost(Post post) throws SQLException;

    int erasePost(Post post) throws SQLException;

    Post votePost(PostLike postLike) throws SQLException, AlreadyVotedException;

    Post cancelVotePost(PostLike postLike) throws SQLException;

//    @Transactional
    int updatePostLike(PostLike postLike, String sign) throws SQLException;

    int increasePostHit(Post post);

//    int increasePostHit(BoardParamBundle boardParamBundle);

    List<BoardViewDto> getIndexView(Paging paging);

    BoardViewDto getSearchResult(BoardParamBundle boardParamBundle);
}