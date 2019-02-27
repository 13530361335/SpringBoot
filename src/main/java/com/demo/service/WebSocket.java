package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {

    private final static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private static Map<String, WebSocket> clients = new ConcurrentHashMap();

    private Session session;

    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.username = username;
        this.session = session;
        clients.put(username, this);
        logger.info(username + "加入连接,在线人数" + clients.size());
        Set<String> online = getClients().keySet();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("online",online);
        sendAll(jsonObject.toJSONString());
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject json = JSONObject.parseObject(message);
        String to = json.getString("to");
        String data = json.getString("data");
        if ("all".equals(to)) {
            sendAll(data);
        } else {
            sendOne(to, data);
        }
    }

    @OnClose
    public void onClose() {
        clients.remove(username);
        logger.info(username + "断开连接,在线人数" + clients.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        clients.remove(username);
        logger.error(error.getMessage(), error);
        logger.info(username + "断开连接,在线人数" + clients.size());
    }

    public void sendOne(String to, String message) {
        WebSocket webSocket = clients.get(to);
        if (webSocket == null) {
            logger.error(to + "不在线");
        } else {
            webSocket.session.getAsyncRemote().sendText(message);
        }
    }

    public void sendAll(String message) {
        clients.forEach((k, v) ->
                v.session.getAsyncRemote().sendText(message)
        );
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }
}