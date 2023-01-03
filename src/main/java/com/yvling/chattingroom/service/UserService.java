package com.yvling.chattingroom.service;

import com.yvling.chattingroom.dao.UsersDao;
import com.yvling.chattingroom.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersDao usersDao;


    // 新增用户
    public void insert_user(String user_name, String user_password, String user_phone) {
        usersDao.insert_user(user_name, user_password, user_phone);
    }

    // 删除用户

    // 修改用户
    public void update_user_info(Integer user_id, String user_name, String user_password, String user_phone) {
        usersDao.update_user_info(user_name, user_password, user_phone, user_id);
    }


    // 查询用户

    public Users select_user_by_id(Integer user_id, String user_password) {
        return usersDao.select_user_by_id(user_id, user_password);
    }
    public Users select_user_by_phone(String user_phone) {
        return usersDao.select_user_by_phone(user_phone);
    }


    public Users select_user_by_phone_and_password(String user_phone, String user_password) {
        return usersDao.select_user_by_phone_and_password(user_phone, user_password);
    }
}
