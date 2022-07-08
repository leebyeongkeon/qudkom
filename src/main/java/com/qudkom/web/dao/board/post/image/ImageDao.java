package com.qudkom.web.dao.board.post.image;

import com.qudkom.web.domain.dto.extended.board.post.image.ImageDto;
import com.qudkom.web.domain.vo.board.post.image.Image;

import java.util.List;

public interface ImageDao {
    List<Image> selectList(Image image);

    List<String> selectFilenameList(Image image);

    int insert(Image image);

    int delete(Image image);

    int deleteList(ImageDto imageDto);
}
