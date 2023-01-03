package com.yvling.chattingroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.yvling.chattingroom.service.InputCheck;
import com.yvling.chattingroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateController {

    @Autowired
    private UserService userService;


    @PostMapping("/update")
    public JSONObject update_user_info(@RequestBody JSONObject request_json) {

        JSONObject response_json = new JSONObject();
        JSONObject update_info = request_json.getJSONObject("update_info");

        Integer user_id = update_info.getInteger("user_id");
        String user_name = update_info.getString("user_name");
        String user_phone = update_info.getString("user_phone");
        String old_password = update_info.getString("old_password");
        String new_password_1 = update_info.getString("new_password_1");
        String new_password_2 = update_info.getString("new_password_2");

        if(InputCheck.has_special_char(user_name) ||
                InputCheck.has_special_char(user_phone) ||
                InputCheck.has_special_char(old_password) ||
                InputCheck.has_special_char(new_password_1) ||
                InputCheck.has_special_char(new_password_2)
        ) {
            response_json.put("status", -1);
            response_json.put("msg", "不能包含特殊字符");
        } else {
            if(!new_password_1.equals(new_password_2)) {
                response_json.put("status", -1);
                response_json.put("msg", "两次输入的密码不一致");
            } else {
                String new_password = new_password_1;
                if(userService.select_user_by_id(user_id, old_password) != null) {
                    userService.update_user_info(user_id, user_name, new_password, user_phone);
                    response_json.put("status", 1);
                    response_json.put("msg", "个人信息修改成功");
                } else {
                    response_json.put("status", -1);
                    response_json.put("msg", "旧密码错误");
                }
            }
        }
        return response_json;
    }
}
