package com.yvling.chattingroom.service;


import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChatBot implements ApplicationRunner {

    private static String url = "http://localhost:5000/send";
    private static String query_type = "txt";
    private static String response;
    private static RestTemplate restTemplate;

    public static String send(String msg) {
        JSONObject request_json = new JSONObject();

        request_json.put("query_type", "txt");
        request_json.put("content", msg);


        try {
            response = restTemplate.postForObject(url, request_json, String.class);
        } catch (Exception e) {
            response = "机器人开小差啦！稍后再试试吧";
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        restTemplate = new RestTemplate();
    }
}
