package com.wjh.constant;

public interface MsgType {
    int LOGIN = 1;
    int LOGIN_ACK = 2;
    int REGISTER = 3;
    int REGISTER_ACK = 4;
    int FORGET_PASSWORD = 5;
    int FORGET_PASSWORD_ACK = 6;
    int CHANGE_PASSWORD = 7;
    int CHANGE_PASSWORD_ACK = 8;
    int SINGLE_MESSAGE = 9;
    int SINGLE_MESSAGE_ACK = 10;
    int GROUP_MESSAGE = 11;
    int GROUP_MESSAGE_ACK = 12;
    int SINGLE_FILE = 13;
    int SINGLE_FILE_ACK = 14;
    int GROUP_FILE = 15;
    int GROUP_FILE_ACK = 16;
    //好友上线通知
    int FRIEND_ONLINE_ADVICE = 17;
    //好友下线通知
    int FRIEND_DISONLINE_ADVICE = 18;
}
