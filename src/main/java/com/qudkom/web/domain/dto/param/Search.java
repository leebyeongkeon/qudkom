package com.qudkom.web.domain.dto.param;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Search {
    private String query;
    private String word;
    private String[] queries;
    private String[] words;

    @Builder
    public Search(String query, String word, String[] queries, String[] words) {
        this.query = query;
        this.word = word;
        this.queries = queries;
        this.words = words;
    }
}
