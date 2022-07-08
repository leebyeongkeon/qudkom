package com.qudkom.web.domain.dto.param.bundle;

import com.qudkom.web.domain.dto.param.FieldBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.dto.param.SqlStr;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@NoArgsConstructor
@Data
public class BoardParamBundle {
    private FieldBundle fieldBundle;

    private Paging paging;
    private Paging commentPaging;
    private Search search;
    private SqlStr sqlStr;

//    private String visitedList;
//    private HttpServletResponse response;

    @Builder
    public BoardParamBundle(FieldBundle fieldBundle, Paging paging, Paging commentPaging, Search search, SqlStr sqlStr) {
        this.fieldBundle = fieldBundle;
        this.paging = paging;
        this.commentPaging = commentPaging;
        this.search = search;
        this.sqlStr = sqlStr;
//        this.visitedList = visitedList;
//        this.response = response;
    }
}
