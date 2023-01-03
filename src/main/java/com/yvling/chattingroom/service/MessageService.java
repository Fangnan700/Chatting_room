package com.yvling.chattingroom.service;

import com.yvling.chattingroom.dao.MessagesDao;
import com.yvling.chattingroom.entity.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessagesDao messagesDao;

    public void insert_message(String user_from, Long message_time, String message_content) {
        messagesDao.insert_message(user_from, message_time, message_content);
    }

    public List<Messages> select_message(Long cur_time) {
        return messagesDao.select_message(cur_time - 1000*60*5);
    }


}
