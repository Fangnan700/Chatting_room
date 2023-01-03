package com.yvling.chattingroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.yvling.chattingroom.service.InputCheck;
import com.yvling.chattingroom.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /*
        用户登录
     */

    // 默认访问
    @RequestMapping("/")
    public void login(HttpServletResponse response) throws Exception {
        response.sendRedirect("/html/login.html");
    }

    // GET请求，跳转到 login.html
    @GetMapping("/login")
    public void login_get(HttpServletResponse response) throws Exception {
        response.sendRedirect("/html/login.html");
    }

    // POST请求，登录操作
    @PostMapping("/login")
    public JSONObject login_post(@RequestBody JSONObject request_json, HttpServletRequest request, HttpServletResponse response) {

        JSONObject response_json = new JSONObject();

        // 提取登录信息
        JSONObject login_info = request_json.getJSONObject("login_info");
        String user_phone = login_info.getString("user_phone");
        String user_password = login_info.getString("user_password");


        if(InputCheck.has_special_char(user_password) || InputCheck.has_special_char(user_phone)) {
            response_json.put("status", -3);
            response_json.put("msg", "不允许含有特殊字符");
        } else {
            if(!InputCheck.phone_check(user_phone)) {
                response_json.put("status", -2);
                response_json.put("msg", "手机格式有误");
            } else {
                if(userService.select_user_by_phone(user_phone) == null) {
                    response_json.put("status", -1);
                    response_json.put("msg", "手机号不存在");
                } else {
                    // 查询用户信息
                    if(userService.select_user_by_phone_and_password(user_phone, user_password) != null) {

                        HttpSession session = request.getSession();
                        session.setAttribute("user_phone", user_phone);
                        Cookie cookie_1 = new Cookie("user_phone", user_phone);
                        Cookie cookie_2 = new Cookie("user_name", userService.select_user_by_phone(user_phone).getUser_name());
                        response.addCookie(cookie_1);
                        response.addCookie(cookie_2);
                        
                        response_json.put("status", 1);
                        response_json.put("msg", "登录成功");
                    } else {
                        response_json.put("status", 0);
                        response_json.put("msg", "密码错误，请重试");
                    }
                }
            }
        }

        return response_json;
    }

}
