package com.qudkom.web.domain.dto.view;

import com.qudkom.web.domain.dto.extended.board.BoardDto;
import com.qudkom.web.domain.dto.extended.board.post.PostDto;
import com.qudkom.web.domain.dto.view.handler.PageHandler;
import com.qudkom.web.domain.vo.board.Board;
import com.qudkom.web.domain.vo.board.BoardCategory;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.PostCategory;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BoardViewDto {
//    private BoardCategory boardCategory;
    private BoardDto boardDto;
//    private int boardNo;
    private List<PostCategory> postCategoryList;
    private PostDto postDto;//Post extends한 dto 정의, commentList, handler를 추가 멤버로
    private List<? extends Post> postList;
    private PageHandler pageHandler;
    private CommentViewDto commentViewDto;

//    private List<Map<BoardDto, List<Post>>>
}
