package com.qudkom.web.domain.vo.board;

import lombok.Data;

import java.util.Objects;

@Data
public class BoardCategory {
    private Integer boardCategoryNo;
    private String categoryName;
    private Integer categoryOrder;
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardCategory that = (BoardCategory) o;
        return Objects.equals(boardCategoryNo, that.boardCategoryNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardCategoryNo);
    }
}
