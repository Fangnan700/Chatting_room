package com.yvling.chattingroom.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yvling.chattingroom.controller.WebSocketController;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Component
public class ChatBot implements ApplicationRunner {

    private static String key;
    private static RestTemplate restTemplate;
    private static ArrayList<Object> messages;

    public static String send(String from, String msg) {

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
        headers.add("Authorization", "Bearer " + key);

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

            String from_name = "Chat_bot";
            long time = System.currentTimeMillis();
            try {
                WebSocketController.messageService.insert_message(from_name, time, content);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "机器人开小差啦";
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassPathResource key_resource = new ClassPathResource("keys.json");
        File key_file = key_resource.getFile();

        JSONObject key_obj = JSON.parseObject(new FileReader(key_file));
        JSONArray keys = (JSONArray) key_obj.get("keys");

        Random random = new Random();
        int index = random.nextInt(keys.size());

        key = keys.getString(index);
        restTemplate = new RestTemplate();
        messages = new ArrayList<>();
    }
}
