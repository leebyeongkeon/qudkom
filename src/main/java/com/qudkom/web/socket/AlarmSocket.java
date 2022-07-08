package com.qudkom.web.socket;

import com.qudkom.web.domain.vo.user.User;
import lombok.extern.java.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Log
@Component
@ServerEndpoint(value = "/alarm",configurator = WebSocketSessionConfigurator.class)
public class AlarmSocket {
//    @Autowired
//    MemoService memoService;
//    @Autowired
//    AlarmService alarmService;
    private Logger logger = LoggerFactory.getLogger(AlarmSocket.class);

    //    private static final Map<Session, User> users= Collections.synchronizedMap(new HashMap<>());
//    private static final Map<Session, HttpSession> users= Collections.synchronizedMap(new HashMap<>());

    private static final Map<Session, HttpSession> sessionMap = Collections.synchronizedMap(new HashMap<>());
    private static final Map<Integer, Session> userMap=Collections.synchronizedMap(new HashMap<>());
//    private static final JSONParser parser=new JSONParser();

    @OnOpen
    public void handleOpen(Session session, EndpointConfig config){
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(WebSocketSessionConfigurator.HTTP_SESSION);
        System.out.println("session.getId() = " + session.getId());
        System.out.println("httpSession.getId() = " + httpSession.getId());
        //쪽지 전송 성공->전송에 성공한 이용자 브라우저에서 소켓 주소로 메시지 전송
        //전달받은 메시지에서 사용자 정보 추출 후 해당 사용자가 사이트에 접속 중인지 체크
        //접속 중이면 해당 사용자 브라우저로 알림
        sessionMap.put(session, httpSession);
        User user=(User)httpSession.getAttribute("user");
        if(user!=null) {
            userMap.put(user.getUserNo(), session);
            System.out.println("userMap = " + userMap);
        }
        // 쪽지 전송, 수신함 insert 완료 후
        // 해당 사용자가 서버에 접속 중인지 확인하고
        //      =>
        // 접속 중이면 메시지 객체를 클라이언트에서 받을 수 있도록
    }
    @OnMessage
    public void handleMessage(String message, Session session) throws ParseException, IOException {
//        HttpSession httpSession=sessionMap.get(session);
        System.out.println("handleMessage");
        System.out.println("session = " + session);

        JSONParser parser=new JSONParser();
        Object obj=parser.parse(message);
        JSONObject jsonObject=(JSONObject)obj;
        System.out.println("jsonObject = " + jsonObject);
        Integer userNo=null;
        if(jsonObject.containsKey("userNo")){
            userNo=Integer.parseInt(jsonObject.get("userNo").toString());
        }
        System.out.println("userNo = " + userNo);
        String type=null;
        if(jsonObject.containsKey("type")) {
            type=(String)jsonObject.get("type");
        }
        System.out.println("msgType = " + type);
        if(userNo!=null){
            System.out.println(userMap);
            Session target=userMap.get(userNo);
            System.out.println("target = " + target);
            if(target!=null){
//                if(memoService==null) System.out.println("memoService = " + memoService);
//                int newMemoCount=memoService.getNewMemoCount(userNo);
                String msg="{\"type\":\""+type+"\",\"alarm\":\"new\"}";
                target.getBasicRemote().sendText(msg);
            }
        }

        //메시지에서 userNo뽑아서 해당 사용자가 서버에 접속되어 있는지 확인한 후에 알람띄우기
//        logger.debug("Sender: "+httpSession.getId()+", Message: "+message);
    }


    @OnError
    public void handleError(Throwable t, Session session){
        t.printStackTrace();
    }
    @OnClose
    public void handleClose(Session session){
        HttpSession httpSession=(HttpSession) sessionMap.get(session);
//        logger.debug("WebSocket session destroyed:" +session.getId());
//        logger.debug("HttpSession destroyed:" +httpSession.getId());
        if(httpSession!=null) {
            User user = (User) httpSession.getAttribute("user");
            if (user != null) {
                userMap.remove(user.getUserNo());
            }
        }
        System.out.println("handleClose");
//        System.out.println("httpSession user id = " + ((User)httpSession.getAttribute("user")).getUserNo());
        System.out.println("session = " + session);
        sessionMap.remove(session);
    }
}
