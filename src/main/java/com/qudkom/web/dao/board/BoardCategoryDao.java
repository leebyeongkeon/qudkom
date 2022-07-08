package com.qudkom.web.dao.board;

import com.qudkom.web.domain.vo.board.BoardCategory;

import java.util.List;

public interface BoardCategoryDao {
    List<BoardCategory> selectBoardCategoryList(int status);
    BoardCategory select(int boardCategoryNo);
}
