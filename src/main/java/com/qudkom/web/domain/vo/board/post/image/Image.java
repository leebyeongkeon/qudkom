package com.qudkom.web.domain.vo.board.post.image;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Image {
    protected Integer boardNo;
    protected Integer postNo;
//    protected Integer imageNo;
    protected String path;
    protected String filename;
    protected String originalFilename;

    @Builder
    public Image(Integer boardNo, Integer postNo, String path, String filename, String originalFilename) {
        this.boardNo = boardNo;
        this.postNo = postNo;
        this.path = path;
        this.filename = filename;
        this.originalFilename = originalFilename;
    }
}
