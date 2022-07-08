package com.qudkom.web.domain.dto.param;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Paging {
    private Integer offset;
    private Integer page;
    private Integer pageSize;
    private Integer listSize;
    private Integer totalCount;

    @Builder
    public Paging(Integer offset, Integer page, Integer pageSize, Integer listSize, Integer totalCount) {
        this.offset = offset;
        this.page = page;
        this.pageSize = pageSize;
        this.listSize = listSize;
        this.totalCount=totalCount;
    }
}
