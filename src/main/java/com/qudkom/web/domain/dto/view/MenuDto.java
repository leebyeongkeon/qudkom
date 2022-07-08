package com.qudkom.web.domain.dto.view;


import com.qudkom.web.domain.vo.board.BoardCategory;
import com.qudkom.web.domain.vo.board.Board;
import lombok.Data;

import java.util.List;

@Data
public class MenuDto {
    private BoardCategory boardCategory;
    private List<Board> boardList;
}
