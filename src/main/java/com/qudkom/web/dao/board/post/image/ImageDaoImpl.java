package com.qudkom.web.dao.board.post.image;

import com.qudkom.web.domain.dto.extended.board.post.image.ImageDto;
import com.qudkom.web.domain.vo.board.post.image.Image;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageDaoImpl implements ImageDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.board.post.image.ImageMapper.";
    @Override
    public List<Image> selectList(Image image){
        return session.selectList(namespace+"selectList", image);
    }
    @Override
    public List<String> selectFilenameList(Image image){
        return session.selectList(namespace+"selectFilenameList",image);
    }

    @Override
    public int insert(Image image){
        return session.insert(namespace+"insert", image);
    }

    @Override
    public int delete(Image image){
        return session.delete(namespace+"delete",image);
    }

    @Override
    public int deleteList(ImageDto imageDto){
        return session.delete(namespace+"deleteList", imageDto);
    }
}
