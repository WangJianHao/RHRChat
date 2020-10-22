package com.wjh.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.constant.MsgType;
import com.wjh.service.RecvService;
import com.wjh.util.JsonUtil;

/**
 * 处理服务器发送过来的信息
 * @author Mr. Wang
 */
public class ClientController {
    static RecvService recvService;
    static String str;
    /**
     * 处理服务器传过来的消息，再执行对应的业务
     * @param msg
     * @return
     */
    public static String process(String msg){
        System.out.println("[RHR client]:客户端接收到服务端消息！");
        recvService = new RecvService();
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int msgType = objectNode.get("msgType").asInt();
        switch (msgType){
            case MsgType.LOGIN_ACK:
                System.out.println("[RHR client]:客户端接收到服务端对登录请求的返回信息！");
                recvService.loginAck(msg);
                break;
            case MsgType.REGISTER_ACK:
                System.out.println("[RHR client]:客户端接收到服务端对注册请求的返回信息！");
                recvService.registerAck(msg);
                break;
            case MsgType.FORGET_PASSWORD_ACK:
                System.out.println("[RHR client]:客户端接收到服务端对忘记密码请求的返回信息！");
                recvService.forgetPasswordAck(msg);
                break;
            case MsgType.CHANGE_PASSWORD_ACK:
                System.out.println("[RHR client]:客户端接收到服务端对更改密码请求的返回信息！");
                recvService.changePasswordAck(msg);
                break;
            case MsgType.FRIEND_ONLINE_ADVICE:
                System.out.println("[RHR client]:好友上线通知！");
                recvService.friendOnline(msg);
                break;
            case MsgType.FRIEND_DISONLINE_ADVICE:
                System.out.println("[RHR client]:好友下线通知！");
                recvService.friendDisOnline(msg);
                break;
            case MsgType.SINGLE_MESSAGE_ACK:
                System.out.println("[RHR client]:接收到好友发送的单聊消息！");
                recvService.receiveSingleMessage(msg);
                break;
            case MsgType.GROUP_MESSAGE_ACK:
                System.out.println("[RHR client]:接收到好友发送的群聊消息！");
                recvService.receiveGroupMessage(msg.substring(0, msg.indexOf("}") + 1));
                break;
            case MsgType.SINGLE_FILE:
                System.out.println("[RHR client]:接收到服务端返回的可供发送文件的端口号");
                recvService.receivePortAndSendFile(msg);
                break;
            case MsgType.SINGLE_FILE_ACK:
                System.out.println("[RHR client]:接收到服务端返回的可供接收文件的端口号");
                recvService.receiveFile(msg);
                break;
                default:
                    break;
        }

        return "";
    }
}
