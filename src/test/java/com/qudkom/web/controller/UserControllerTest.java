package com.qudkom.web.controller;

import com.qudkom.web.domain.vo.user.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc=MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testMockMvc() throws Exception{

        User user= User.builder()
                .nickname("you")
                .id("newUser")
                .password("1234")
                .build();

        ResultActions actions = mockMvc.perform(post("/user/register")
                        .param("id",user.getId())
                        .param("password",user.getPassword())
                        .param("nickname",user.getNickname())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                        .andDo(print());
    }

    @Test
    public void testGetRegisterPage() throws Exception{
        ResultActions actions=mockMvc.perform(get("/user/register"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}