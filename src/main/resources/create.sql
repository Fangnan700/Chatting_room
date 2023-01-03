-- ChattingRoom 项目数据库
-- 作者：方楠

-- 创建数据库
CREATE DATABASE IF NOT EXISTS chatting_room DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 切换数据库
USE chatting_room;

-- 创建数据表

## 用户信息表
CREATE TABLE IF NOT EXISTS users
(
    user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(128) NOT NULL,
    user_password VARCHAR(128) NOT NULL,
    user_phone VARCHAR(128) NOT NULL
);

## 消息记录表
CREATE TABLE IF NOT EXISTS messages
(
    message_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    message_from VARCHAR(64),
    message_time LONG,
    message_content MEDIUMTEXT
);