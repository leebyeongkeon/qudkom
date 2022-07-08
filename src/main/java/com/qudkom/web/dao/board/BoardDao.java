package com.qudkom.web.dao.board;

import com.qudkom.web.domain.dto.extended.board.BoardDto;
import com.qudkom.web.domain.vo.board.Board;
import com.qudkom.web.domain.vo.board.BoardCategory;

import java.util.List;

public interface BoardDao {
    Board selectBoardByBoardNo(int boardNo)//게시판 기본 정보
    ;

    List<Board> selectAccessibleBoardList(BoardCategory boardCategory)//일반 유저가 접근할 수 있는 게시판 목록
    ;

    List<Board> selectBoardList();

    int selectBoardCategoryNo(int boardNo);

    List<BoardDto> selectIndexBoards(int status);
}
