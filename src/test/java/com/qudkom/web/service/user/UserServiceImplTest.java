package com.qudkom.web.service.user;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Test
    public void testSendMail()throws Exception{
//        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
//        JSONObject json=userService.sendMail("kyle0223@naver.com", null);
//        System.out.println("json = " + json);
    }

}