package com.wjh.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.wjh.constant.MsgType;
import com.wjh.service.*;
import com.wjh.util.CacheUtil;
import com.wjh.util.JsonUtil;
import com.wjh.util.PortUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;


import java.sql.SQLException;

public class NettyController {
    private static UserService userService;
    private static  MessageService messageService = new MessageService();
    private static GroupMessageService groupMessageService = new GroupMessageService();
    /**
     * 服务端接收到的json消息都在该方法处理
     */
    public static String process(String msg,ChannelHandlerContext ctx) throws Exception {
        userService = new UserService(ctx.channel());
        //解析客户端发送的消息
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int msgType = objectNode.get("msgType").asInt();
        switch (msgType){
            //登录信息处理
            case MsgType.LOGIN:
                System.out.println("[RHR server]:登录请求信息");
                int id = objectNode.get("id").asInt();
                String password = objectNode.get("password").asText();
                String recv = userService.login(id, password);
                return recv;
            //注册信息处理
            case MsgType.REGISTER:
                System.out.println("[RHR server]:注册请求信息");
                String email = objectNode.get("email").asText();
                String userName = objectNode.get("userName").asText();
                password = objectNode.get("password").asText();
                recv = userService.register(email,userName,password);
                return recv;
            case MsgType.FORGET_PASSWORD:
                System.out.println("[RHR server]:忘记密码请求信息");
                id = objectNode.get("id").asInt();
                recv = userService.forgetPassword(id);
                return  recv;
            case MsgType.CHANGE_PASSWORD:
                System.out.println("[RHR server]:客户端请求更改密码");
                id = objectNode.get("id").asInt();
                String oldPassword= objectNode.get("oldPassword").asText();
                String newPassword= objectNode.get("newPassword").asText();
                //如果成功的话要清除缓存，而且客户端也会退出并重新登录
                recv = userService.changePassword(id,oldPassword,newPassword);
                return recv;
            case MsgType.SINGLE_MESSAGE:
                System.out.println("[RHR server]:客户端发送消息");
                //执行发送消息的业务逻辑
                recv = messageService.singleMessage(msg);
                return recv;
            case MsgType.GROUP_MESSAGE:
                System.out.println("[RHR server]:客户端发送群聊消息");
                //执行发送群聊消息的业务逻辑
                recv = groupMessageService.groupMessage(msg);
                return recv;
            case MsgType.SINGLE_FILE:
                System.out.println("[RHR server]:客户端发送文件");
                //发送文件操作
                return fileTransfer(msg);
            case MsgType.GROUP_FILE:
                System.out.println("[RHR server]:客户端群发文件");
                //群发文件操作
                return groupFileTransfer(msg);
            default:
                break;
        }
        return "";
    }

    /**
     * 群发消息思想
     * 开辟一个发送端的端口让发送方先将文件存储在服务端
     * 然后每个群里的其他成员才能接收
     * objectNode.put("msgType",MsgType.GROUP_FILE);
     * objectNode.put("sender",user.getId());
     * objectNode.put("group_id",group.getGroupId());
     * objectNode.put("path",path);
     * objectNode.put("time",time);
     * @param msg
     * @return
     */
    private static String groupFileTransfer(String msg){
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int sender = objectNode.get("sender").asInt();
        int group_id = objectNode.get("group_id").asInt();
        String path = objectNode.get("path").asText();
        String time = objectNode.get("time").asText();
        int fromPort = PortUtil.getFreePort();
        System.out.println("[RHR server]:服务端分配端口给发送端");

        //封装返回Json
        ObjectNode obj = JsonUtil.getObjectNode();
        obj.put("msgType",MsgType.SINGLE_FILE);
        obj.put("port",fromPort);
        obj.put("path",path);
        //启动子线程接收发送端的文件
        new AcceptGroupFileService(fromPort,sender,group_id,time).start();

        return obj.toString();
    }
    //文件传输功能
    private static String fileTransfer(String msg){
        //解析JSON 接收方是否在线
        //在线
        // 分配端口：fromPort，toPort
        //启动子线程 参数：fromPort，toPort   启动服务进行监听
        //给接收方推送端口
        //给发送方发送端口
        //不在线
        //分配端口fromPort
        //启动子线程 参数fromPort  监听发送端的连接   存储文件
        //给发送端发送端口
        //接收端上线后
        //服务端推送端口给接收端
        //创建子线程将文发送给子线程
        System.out.println("[RHR server]:执行文件转发业务！");
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int sender = objectNode.get("sender").asInt();
        int receiver = objectNode.get("receiver").asInt();
        String time = objectNode.get("time").asText();
        //给发送方分配一个端口

        int fromPort = PortUtil.getFreePort();
        System.out.println("[RHR server]:服务端分配端口给发送端");

        //封装返回Json
        ObjectNode obj = JsonUtil.getObjectNode();
        obj.put("msgType",MsgType.SINGLE_FILE);
        obj.put("port",fromPort);
        obj.put("path",objectNode.get("path").asText());
        //判断是否在线
        if(CacheUtil.isOnline(receiver)){
            System.out.println("[RHR server]:接收方在线！");
            //接收端在线
            Channel channelById = CacheUtil.getChannelById(receiver);
            //给接收方分配一个端口
            int toPort = PortUtil.getFreePort();
            //启动子线程
            new TransferFileService(fromPort,toPort).start();
            System.out.println("[RHR server]:服务端子线程启动！");
            //给接收方发送Json数据
            ObjectNode receiverNode = JsonUtil.getObjectNode();
            receiverNode.put("msgType",MsgType.SINGLE_FILE_ACK);
            receiverNode.put("sender",sender);
            receiverNode.put("port",toPort);
            //给接收端发送端口
            if(channelById != null) {
                channelById.writeAndFlush(receiverNode.toString());
            }
        }else{
            //接收方不在线
            System.out.println("[RHR server]:接收方不在线！");
            //启动子线程接收发送端的文件
            new TransferFileService(fromPort,0,sender,receiver,time).start();
        }
        //给发送端发送端口
        return obj.toString();

    }
}
