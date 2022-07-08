package com.qudkom.web.domain.dto.extended.board;

import com.qudkom.web.domain.vo.board.Board;
import lombok.Data;

@Data
public class BoardDto extends Board {
    private String categoryName;

    public BoardDto(Board board, String categoryName){
        this(board.getBoardNo(),board.getBoardCategoryNo(),board.getBoardName(),board.getStatus(),board.getBoardOrder(),categoryName);
    }
    public BoardDto(Integer boardNo, Integer boardCategoryNo, String boardName, Integer status, Integer boardOrder, String categoryName) {
        super(boardNo, boardCategoryNo, boardName, status, boardOrder);
        this.categoryName = categoryName;
    }
}
