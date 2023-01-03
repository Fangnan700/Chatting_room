package com.yvling.chattingroom.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputCheck {

    public static boolean phone_check(String phone) {
        String PHONE_FORMAT = "^[1][3,4,5,8][0-9]{9}$";
        Pattern regex = Pattern.compile(PHONE_FORMAT);
        Matcher matcher = regex.matcher(phone);
        return matcher.matches();
    }

    public static boolean has_special_char(String str) {
        String SPECIAL_CHAR = "^[a-zA-Z0-9\\u4E00-\\u9FFF]+$";
        Pattern regex = Pattern.compile(SPECIAL_CHAR);
        Matcher matcher = regex.matcher(str);
        return !matcher.matches();
    }
}
