package com.qudkom.web.socket;

//import javax.websocket.ClientEndpointConfig.Configurator;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@Configuration
public class WebSocketSessionConfigurator extends Configurator {
    public static final String HTTP_SESSION="PRIVATE_HTTP_SESSION";
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        super.modifyHandshake(sec, request, response);
        HttpSession session=(HttpSession) request.getHttpSession();
        if(session!=null){
            sec.getUserProperties().put(HTTP_SESSION,session);
        }
    }
}
