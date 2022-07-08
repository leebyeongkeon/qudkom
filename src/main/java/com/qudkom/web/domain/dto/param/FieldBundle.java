package com.qudkom.web.domain.dto.param;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FieldBundle {
    private Integer boardNo;
    private Integer postNo;
    private Integer commentNo;
    private Integer userNo;
    private Integer categoryNo;
    private Integer status;
    private Boolean isLike;


    @Builder
    public FieldBundle(Integer boardNo, Integer postNo, Integer commentNo, Integer userNo, Integer categoryNo, Integer status, Boolean isLike) {
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.commentNo = commentNo;
        this.userNo = userNo;
        this.categoryNo = categoryNo;
        this.status = status;
        this.isLike = isLike;
    }
}
