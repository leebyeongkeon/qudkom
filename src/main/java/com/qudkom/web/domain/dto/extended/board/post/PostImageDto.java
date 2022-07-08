package com.qudkom.web.domain.dto.extended.board.post;

import com.qudkom.web.domain.vo.board.post.Post;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostImageDto {
    private Post post;
    private List<MultipartFile> file;
}
