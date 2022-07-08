package com.qudkom.web.dto.handler;

import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class CommentPageHandlerTest {

    @Test
    public void testCommentPageHandler() throws Exception{
        List<Comment> list=new ArrayList<>();
        for(int i=0;i<89;i++)
            list.add(new Comment());
        CommentPageHandler ph=new CommentPageHandler(list,10,5);
        ph.setCommentPageByPaging(4);
        list=ph.extractCommentList();
        System.out.println("commentPageHandler : "+ph);
    }
}