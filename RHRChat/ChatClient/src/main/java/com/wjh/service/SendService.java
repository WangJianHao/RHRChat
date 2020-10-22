package com.wjh.service;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.constant.MsgType;
import com.wjh.util.JsonUtil;
import io.netty.channel.Channel;

import java.util.Scanner;

public class SendService {
    public static  Channel channel;


    /**
     * 刚刚开始写本项目时测试发送消息的方法
     * 无参
     */
    public void send(){
        Scanner scanner = new Scanner(System.in);
        String msg = "";
        while (scanner.hasNext()){
            msg = scanner.nextLine();
            channel.writeAndFlush(msg);
        }
    }

    /**
     * 登录功能
     * @param idStr
     * @param password
     * @throws Exception
     */
    public void login(String idStr,String password) throws Exception {
        if(idStr.equals("")){
            throw new Exception("未输入账号");
        }else if(password.equals("")){
            throw new Exception("未输入密码");
        }
        int id = Integer.parseInt(idStr);
        //封装给服务端JSON msg  msgType
        ObjectNode objectNode = JsonUtil.getObjectNode();
        //该json数据用于发送登录信息
        objectNode.put("msgType",MsgType.LOGIN);
        objectNode.put("id",id);
        objectNode.put("password",password);

        //将账号和密码发送给服务端
        channel.writeAndFlush(objectNode.toString());

    }

    /**
     * 注册功能
     * @param email
     * @param userName
     * @param password
     */
    public void register(String email, String userName, String password) throws Exception {
        if(email.equals("")){
            throw new Exception("未输入账号");
        }else if(password.equals("")){
            throw new Exception("未输入密码");
        }else if(userName.equals("")){
            throw new Exception("未输入用户名");
        }

        //封装为JSON
        ObjectNode objectNode = JsonUtil.getObjectNode();
        //将消息类型和内容放入
        objectNode.put("msgType",MsgType.REGISTER);
        objectNode.put("email",email);
        objectNode.put("userName",userName);
        objectNode.put("password",password);

        //将邮箱，用户名，密码发送给服务端
        channel.writeAndFlush(objectNode.toString());
    }

    /**
     * 忘记密码的客户端业务处理
     * @param idStr
     */
    public void forgetPassword(String idStr) throws Exception {
        int id = 0;
        try{
             id = Integer.parseInt(idStr);
        }catch (NumberFormatException e){
            throw  new Exception("账号输入错误的字符！");
        }
        ObjectNode objectNode = JsonUtil.getObjectNode();
        //确认发送的数据类型
        objectNode.put("msgType",MsgType.FORGET_PASSWORD);
        //将id发送进去
        objectNode.put("id",id);

        //打印日志
        System.out.println("[client]:忘记密码的请求已发送，"+id);

        //将id发送给服务端
        channel.writeAndFlush(objectNode.toString());
    }
}
