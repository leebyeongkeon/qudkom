package com.qudkom.web.domain.dto.extended.board.post.image;

import com.qudkom.web.domain.vo.board.post.image.Image;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ToString
@Data
public class ImageDto extends Image {
    public static final String TEMP_DIRECTORY ="/temp/";
    public static final String UPLOAD_DIRECTORY ="/upload/";

//    public static final String PATH_PROPERTIES="path.properties";

    public static final String TEMP_REAL_PATH;
    public static final String UPLOAD_REAL_PATH;
    static{
//        Properties properties=new Properties();
//        try {
//            Reader reader = Resources.getResourceAsReader(PATH_PROPERTIES);
//            properties.load(reader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        TEMP_REAL_PATH =properties.getProperty("upload.temp");
//        SAVE_REAL_PATH =properties.getProperty("upload.save");

        TEMP_REAL_PATH =System.getProperty("user.home")+TEMP_DIRECTORY;
        UPLOAD_REAL_PATH =System.getProperty("user.home")+UPLOAD_DIRECTORY;
    }
    private List<String> filenames;
    public ImageDto(){
        filenames=new ArrayList<>();
    }
//    private Integer fileSize;

}
