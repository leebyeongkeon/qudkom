package com.qudkom.web.domain.dto.view.handler;

import lombok.Data;

@Data
public class AlarmPageHandler extends PageHandler {
    public static final int DEFAULT_SINGLE_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_LIST_SIZE = 5;

    public AlarmPageHandler(int totalCount, int currentPage) {
        this(totalCount, currentPage, DEFAULT_SINGLE_PAGE_SIZE, DEFAULT_PAGE_LIST_SIZE);
    }

    public AlarmPageHandler(int totalCount, int currentPage, int pageSize, int listSize) {
        super(totalCount, currentPage, pageSize, listSize);
    }
}