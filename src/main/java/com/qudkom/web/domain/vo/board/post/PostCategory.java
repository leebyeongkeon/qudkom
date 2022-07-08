package com.qudkom.web.domain.vo.board.post;

import lombok.Builder;
import lombok.Data;

@Data
public class PostCategory {
    private Integer boardCategoryNo;
    private Integer postCategoryNo;
    private String categoryName;

    @Builder
    public PostCategory(Integer boardCategoryNo, Integer postCategoryNo, String categoryName) {
        this.boardCategoryNo = boardCategoryNo;
        this.postCategoryNo = postCategoryNo;
        this.categoryName = categoryName;
    }
}
