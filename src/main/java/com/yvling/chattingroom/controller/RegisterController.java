package com.yvling.chattingroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.yvling.chattingroom.entity.Users;
import com.yvling.chattingroom.service.InputCheck;
import com.yvling.chattingroom.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    /*
        用户注册
     */

    // GET请求，跳转到 register.html
    @GetMapping("/register")
    public void register_get(HttpServletResponse response) throws Exception {
        response.sendRedirect("/html/register.html");
    }

    // POST请求，进行注册操作
    @PostMapping("/register")
    public JSONObject register_post(@RequestBody JSONObject request_json) {

        JSONObject response_json = new JSONObject();

        // 提取请求注册的用户信息
        JSONObject register_info = request_json.getJSONObject("register_info");
        String user_name = register_info.getString("user_name");
        String user_password = register_info.getString("user_password");
        String user_phone = register_info.getString("user_phone");

        if(InputCheck.has_special_char(user_name) || InputCheck.has_special_char(user_password) || InputCheck.has_special_char(user_phone)) {
            response_json.put("status", -1);
            response_json.put("msg", "不允许含有特殊字符");
        } else {
            if(!InputCheck.phone_check(user_phone)) {
                response_json.put("status", -2);
                response_json.put("msg", "手机格式有误");
            } else {
                Users pre_user = userService.select_user_by_phone(user_phone);
                if(pre_user != null) {
                    response_json.put("status", 0);
                    response_json.put("msg", "手机号已注册");
                } else {
                    userService.insert_user(user_name, user_password, user_phone);
                    response_json.put("status", 1);
                    response_json.put("msg", "注册成功");
                }
            }
        }

        return response_json;
    }
}
