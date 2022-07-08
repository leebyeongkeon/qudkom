package com.qudkom.web.util;

import com.qudkom.web.domain.dto.extended.board.post.image.ImageDto;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.Reader;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class ResourceTest {
    @Autowired
    ResourceLoader loader;
    @Test
    public void testGetResource() throws Exception{
        String location="file:C:/Dev/apache-tomcat-9.0.56/temp/upload/";
        String filename="1656143692533794237.png";
//        Resource resource=loader.getResource(location+filename);
        Resource resource=loader.getResource("file:temp/upload/img2.jpg");
        System.out.println("resource.getURI() = " + resource.getURI());
        System.out.println("resource.getURL() = " + resource.getURL());
        assertTrue(resource.exists());
        //임시 디렉토리에 저장
        //게시물 저장시 이미지 element 추출-> 소스 값들 뽑아서 resourceloader로 존재 유무 등을 확인하고
        //파일을 옮김
        //
    }
    @Test
    public void testReadFileWithPath() throws Exception{
//        String path=ImageDto.TEMP_DIRECTORY;
        String path=System.getProperty("user.dir");
        System.out.println("path = " + path);
        File f=new File(path);
        System.out.println("f.getAbsolutePath() = " + f.getAbsolutePath());
//        String path= ImageDto.TEMP_PATH + "16561418801764829.png";
        File file=new File("temp/upload/img2.jpg");


        System.out.println("file.exists() = " + file.exists());
        System.out.println("file.isDirectory() = " + file.isDirectory());
        System.out.println("file.getParent() = " + file.getParent());
        System.out.println("file.getPath() = " + file.getPath());
        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        System.out.println("file.getCanonicalPath() = " + file.getCanonicalPath());

        File dest=new File(ImageDto.UPLOAD_REAL_PATH +"1/"+file.getName());
        System.out.println("dest.exists() = " + dest.exists());

//        FileUtils.moveDirectory(file,dest);
//        FileUtils.moveFile(file,dest);

    }
    @Test
    public void testProperties() throws Exception{
        Properties properties=new Properties();
        String resource = "path.properties";
        Reader reader= Resources.getResourceAsReader(resource);
        properties.load(reader);
        String path=properties.getProperty("uploadPath.temp");
        System.out.println("path = " + path);
    }

    @Test
    public void moveFile() throws Exception{
        String path=ImageDto.TEMP_REAL_PATH;
        String filename="새 문서.doc";
        File file=new File(path+filename);
        File dest=new File(ImageDto.UPLOAD_REAL_PATH +filename);
        FileUtils.moveFile(file, dest);
        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        System.out.println("dest.getName() = " + dest.getName());
        System.out.println("dest.getAbsolutePath() = " + dest.getAbsolutePath());
    }
}
