package com.yvling.chattingroom.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yvling.chattingroom.dao.MessagesDao;
import com.yvling.chattingroom.entity.Messages;
import com.yvling.chattingroom.service.ChatBot;
import com.yvling.chattingroom.service.MessageService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{user_phone}/{user_name}")
public class WebSocketController implements ApplicationRunner {


    public static MessageService messageService;
    public static MessagesDao messagesDao;
    @Autowired
    public void setMessageService(MessageService messageService1) {
        WebSocketController.messageService = messageService1;
    }
    @Autowired
    public void setMessagesDao(MessagesDao messagesDao1) {
        WebSocketController.messagesDao = messagesDao1;
    }


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String user_phone;
    private String user_name;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据(jakarta.websocket.Session)
    private Session session;

    // 保存所有连接的 websocket (WebSocket指的是当前类)
    private static CopyOnWriteArrayList<WebSocketController> webSocketsPool;

    // 保存在线的连接数
    private static Map<String, Session> sessionPool;


    // 连接建立时的操作
    @OnOpen
    public void onOpen(Session session, @PathParam("user_phone") String user_phone, @PathParam("user_name") String user_name) {
        try {
            this.session = session;
            this.user_phone = user_phone;
            this.user_name = URLDecoder.decode(user_name);
            webSocketsPool.add(this);
            sessionPool.put(this.user_phone, session);

            JSONObject message_obj = new JSONObject();
            message_obj.put("who_join", this.user_name);
            message_obj.put("message", "1_open");
            message_obj.put("from_name", "system");
            sendAllMessage(message_obj.toString());

            System.out.println("[websocket消息] " + this.user_name + "已连接，在线总数为:" + webSocketsPool.size());
        } catch (Exception e) {
        }
    }

    // 连接关闭时的操作
    @OnClose
    public void onClose() {
        try {
            webSocketsPool.remove(this);
            sessionPool.remove(this.user_phone);

            JSONObject message_obj = new JSONObject();
            message_obj.put("who_leave", this.user_name);
            message_obj.put("message", "1_close");
            message_obj.put("from_name", "system");

            sendAllMessage(message_obj.toString());

            System.out.println("[websocket消息] " + this.user_name + "的连接断开，在线总数为:" + webSocketsPool.size());
        } catch (Exception e) {
        }
    }

    // 收到客户端消息后的操作
    @OnMessage
    public void onMessage(String message_str) {
        JSONObject message_obj = JSON.parseObject(message_str);

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendAllMessage(message_obj.toString());
        System.out.println("[websocket消息] 收到客户端消息:" + message_obj);


        Long time = message_obj.getLong("time");
        String from_name = message_obj.getString("from_name");
        String from_phone = message_obj.getString("from_phone");
        String message = message_obj.getString("message");



        try {
            WebSocketController.messageService.insert_message(from_name, time, message);
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(message.length() >= 4) {
            String hint = message.substring(0, 4);
            if(!from_name.equals("system") && hint.equals("@bot")) {
                String response_msg = ChatBot.send(from_name, message.substring(4));

                JSONObject response_json = new JSONObject();
                response_json.put("time", System.currentTimeMillis());
                response_json.put("from_name", "Chat_bot");
                response_json.put("message", response_msg.trim());

                sendAllMessage(response_json.toString());

                System.out.println("[websocket消息] ChatBot响应:" + response_msg.trim());
            }
        }


        if(message.length() >= 11 && message.substring(0,11).equals("get_history")) {
            // 获取历史消息
            Long cur_time = System.currentTimeMillis();

            try {
                List<Messages> history_messages = WebSocketController.messageService.select_message(cur_time);
                for(Messages message_ : history_messages) {
                    JSONObject history_message_obj = new JSONObject();
                    history_message_obj.put("time", message_.getMessage_time());
                    history_message_obj.put("from_name", message_.getMessage_from());
                    history_message_obj.put("message", message_.getMessage_content());
                    System.out.println(history_message_obj);
                    sendOneMessage(from_phone, history_message_obj.toString());
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 异常时的操作
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("[websocket消息] 出现异常:" + error.getMessage());
    }


    // 广播消息操作

    // 此为广播消息
    public void sendAllMessage(String message) {
        System.out.println("[websocket消息] 广播消息:"+message);
        for(WebSocketController webSocket : webSocketsPool) {
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String user_phone, String message) {
        Session session = sessionPool.get(user_phone);
        if (session != null&&session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                System.out.println("【websocket消息】 单点消息:"+message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        webSocketsPool = new CopyOnWriteArrayList<>();
        sessionPool = new HashMap<>();
    }

    public static Integer get_online_numbers() {
        return webSocketsPool.size();
    }
}
