package com.qudkom.web.dto.handler;

import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import com.qudkom.web.domain.dto.view.handler.PageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class PageHandlerTest {
    
    @Test
    public void testPageHandler() throws Exception{
        PageHandler ph=new PageHandler(3010, 151,20, 10);
        System.out.println("ph = " + ph);
        List<Integer> list=ph.getPageList();
        list.stream().forEach(i->System.out.printf("%d ",i));
        System.out.println();
    }
    @Test
    public void testCommentPageHandler() throws Exception{
        List<Comment> list=new ArrayList<>();
        for(int i=0;i<240;i++)
            list.add(new Comment());
        CommentPageHandler cph=new CommentPageHandler(list);
        cph.setCommentPageByPaging(1);
        System.out.println("cph = " + cph);
    }
}