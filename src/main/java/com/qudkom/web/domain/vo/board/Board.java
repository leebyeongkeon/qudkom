package com.qudkom.web.domain.vo.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Board {
    protected Integer boardNo;
    protected Integer boardCategoryNo;
    protected String boardName;
    protected Integer status;
    protected Integer boardOrder;

}
