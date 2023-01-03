package com.yvling.chattingroom.dao;

import com.yvling.chattingroom.entity.Users;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersDao {

    /*
        新增用户
     */
    @Insert("INSERT INTO users(user_name, user_password, user_phone) VALUES (#{user_name}, #{user_password}, #{user_phone})")
    void insert_user(
            @Param("user_name") String user_name,
            @Param("user_password") String user_password,
            @Param("user_phone") String user_phone
    );

    /*
        删除用户
     */


    /*
        修改用户
     */
    @Update("UPDATE users SET user_name = #{user_name}, user_password = #{user_password}, user_phone = #{user_phone} WHERE user_id = #{user_id}")
    void update_user_info(
            @Param("user_name") String user_name,
            @Param("user_password") String user_password,
            @Param("user_phone") String user_phone,
            @Param("user_id") Integer user_id
    );


    /*
        查询用户
     */
    // 根据用户ID查询
    @Select("SELECT * FROM users WHERE user_id = #{user_id} AND user_password = #{user_password}")
    Users select_user_by_id(@Param("user_id") Integer user_id, @Param("user_password") String user_password);

    // 根据用户手机号查询
    @Select("SELECT * FROM users WHERE user_phone = #{user_phone}")
    Users select_user_by_phone(@Param("user_phone") String user_phone);

    // 根据用户手机号和密码查询
    @Select("SELECT * FROM users WHERE user_phone = #{user_phone} AND user_password = #{user_password}")
    Users select_user_by_phone_and_password(@Param("user_phone") String user_phone, @Param("user_password") String user_password);

}
