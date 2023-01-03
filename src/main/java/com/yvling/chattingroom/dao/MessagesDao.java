package com.yvling.chattingroom.dao;


import com.yvling.chattingroom.entity.Messages;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessagesDao {

    // 新增消息
    @Insert("INSERT INTO messages(message_from, message_time, message_content) VALUES (#{message_from}, #{message_time}, #{message_content})")
    void insert_message(@Param("message_from") String user_from, @Param("message_time") Long message_time, @Param("message_content") String message_content);

    // 查询1小时内的消息
    @Select("SELECT * FROM messages WHERE message_time > #{time}")
    List<Messages> select_message(@Param("time") Long time);

}
