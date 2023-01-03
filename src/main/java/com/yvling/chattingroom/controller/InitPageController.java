package com.yvling.chattingroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.yvling.chattingroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitPageController {

    @Autowired
    private UserService userService;

    @PostMapping("/query_user_info")
    public JSONObject query_user_info(@RequestBody JSONObject request_json) {

        JSONObject response_json = new JSONObject();

        // 提取请求信息
        JSONObject query_info = request_json.getJSONObject("query_info");
        String user_phone = query_info.getString("user_phone");

        response_json.put("user_id", userService.select_user_by_phone(user_phone).getUser_id());
        response_json.put("user_name", userService.select_user_by_phone(user_phone).getUser_name());
        response_json.put("user_phone", userService.select_user_by_phone(user_phone).getUser_phone());
        response_json.put("online_number", WebSocketController.get_online_numbers());
        return response_json;
    }
}
