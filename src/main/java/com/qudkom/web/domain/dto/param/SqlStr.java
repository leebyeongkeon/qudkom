package com.qudkom.web.domain.dto.param;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SqlStr {
    private String sign;
    private String field;

    @Builder
    public SqlStr(String sign, String field) {
        this.sign = sign;
        this.field = field;
    }
}
