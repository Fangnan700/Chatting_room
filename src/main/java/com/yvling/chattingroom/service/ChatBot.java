package com.yvling.chattingroom.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component
public class ChatBot implements ApplicationRunner {

    private static RestTemplate restTemplate;
    private static ArrayList<Object> messages;

    public static String send(String from, String msg) {
//
//        restTemplate = new RestTemplate();
//        messages = new ArrayList<>();

        // 创建消息体
        Map<String, String> u_message = new HashMap<>();
        u_message.put("role", "user");
        u_message.put("content", msg);

        messages.add(u_message);
        if(messages.size() >= 20) {
            messages = (ArrayList<Object>) messages.subList(messages.size() - 20, messages.size());
        }

        // 发送请求
        String url = "http://23.224.121.246:9999";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer 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");

        Map<String, Object> body = new HashMap<>();

        body.put("model", "gpt-3.5-turbo");
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            JSONObject r = JSONObject.parseObject(response.getBody());
            String content = r.getJSONArray("choices").getJSONObject(0).getJSONObject("message").get("content").toString();

            // 创建消息体
            Map<String, String> a_message = new HashMap<>();
            a_message.put("role", "user");
            a_message.put("content", content);

            messages.add(a_message);

            return content;
        } catch (Exception e) {
            return "机器人开小差啦";
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        restTemplate = new RestTemplate();
        messages = new ArrayList<>();
    }
}
