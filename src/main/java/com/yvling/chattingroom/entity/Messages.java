package com.yvling.chattingroom.entity;

public class Messages {
    private Integer message_id;
    private String message_from;
    private Long message_time;
    private String message_content;

    public Messages() {}

    public Messages(String message_from, Long message_time, String message_content) {
        this.message_from = message_from;
        this.message_time = message_time;
        this.message_content = message_content;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public String getMessage_from() {
        return message_from;
    }

    public Long getMessage_time() {
        return message_time;
    }

    public String getMessage_content() {
        return message_content;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "message_id=" + message_id +
                ", message_from='" + message_from + '\'' +
                ", message_time=" + message_time +
                ", message_content='" + message_content + '\'' +
                '}';
    }
}
