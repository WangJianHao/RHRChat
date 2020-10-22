package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.ChatGroup;
import com.wjh.bean.User;
import com.wjh.controller.ClientController;
import com.wjh.frame.FriendListFrame;
import com.wjh.frame.GroupChatFrame;
import com.wjh.frame.SingleChatFrame;
import com.wjh.netty.NettyClient;
import com.wjh.util.JsonUtil;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;


/**
 * 根据服务器的回复的消息类型执行对应的方法
 */
public class RecvService {


    /**
     * 接收到服务器返回的登录回复
     */
    public void loginAck(String msg){
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        boolean status = objectNode.get("login_status").asBoolean();
        String status_msg = objectNode.get("status_msg").asText();
        JOptionPane.showMessageDialog(null,status_msg);
        if(status){
            User user = new User();
            user.setId(objectNode.get("id").asInt());
            user.setUserName(objectNode.get("userName").asText());
            user.setPassword(objectNode.get("password").asText());
            user.setEmail(objectNode.get("email").asText());
            //登录成功创建好友列表界面   好友列表不显示在线，这个由服务器负责判断
            String mates   = objectNode.get("mates").asText();
            String family = objectNode.get("family").asText();
            String define = objectNode.get("define").asText();
            //格式为 group_id,user1,user2,user3,user4,user5   不存在的用""
            int count = objectNode.get("count").asInt();
            HashMap<Integer, ChatGroup> groups = new HashMap<>();
            for(int i = 1;i<=count;i++){
                String group = objectNode.get("group" + i).asText();
                String[] split = group.split(",");
                ChatGroup chatGroup = new ChatGroup();
                chatGroup.setGroupId(Integer.parseInt(split[0]));
                //群里面至少有两个人
                chatGroup.setUser1(Integer.parseInt(split[1]));
                chatGroup.setUser2(Integer.parseInt(split[2]));
                //
                if(!split[3].equals("-1")){
                    chatGroup.setUser3(Integer.parseInt(split[3]));
                }
                if(!split[4].equals("-1")){
                    chatGroup.setUser3(Integer.parseInt(split[4]));
                }
                if(!split[5].equals("-1")){
                    chatGroup.setUser3(Integer.parseInt(split[5]));
                }
                groups.put((int)chatGroup.getGroupId(),chatGroup);
            }
            NettyClient.friendListFrame =  new FriendListFrame(user,mates,family,define,groups);
            //在线服务的管道赋值
            OnlineService.channel = SendService.channel;
            NettyClient.loginFrame.dispose();
        }
    }

    /**
     * 接收服务器返回的注册回复
     * @param msg
     */
    public void registerAck(String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        boolean status = objectNode.get("register_status").asBoolean();
        String status_msg = objectNode.get("status_msg").asText();
        JOptionPane.showMessageDialog(null,status_msg);
        if(status){
            NettyClient.loginFrame.setVisible(true);
            NettyClient.registerFrame.dispose();
        }
    }

    /**
     * 接收服务器返回的忘记密码的回复
     * @param msg
     */
    public void forgetPasswordAck(String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        boolean status = objectNode.get("forgetPassword_status").asBoolean();
        String status_msg = objectNode.get("status_msg").asText();
        JOptionPane.showMessageDialog(null,status_msg);
        if(status){
            NettyClient.loginFrame.setVisible(true);
            NettyClient.forgetPasswordFrame.dispose();
        }
    }


    /**
     * 接收服务器返回的更改密码的回复
     * @param msg
     */
    public void changePasswordAck(String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        boolean status = objectNode.get("changePassword_status").asBoolean();
        String status_msg = objectNode.get("status_msg").asText();
        JOptionPane.showMessageDialog(null,status_msg);
        if(status){
            NettyClient.changePasswordFrame.dispose();
            //更改密码后系统要重新登录.
            System.exit(0);
        }
    }

    /**
     * 好友上线通知
     * @param msg
     */
    public void friendOnline(String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int friend_id = objectNode.get("friend_id").asInt();
        User user = FriendListFrame.friendList.get(friend_id);
        JOptionPane.showMessageDialog(null,"好友"+user.getUserName()+"上线了！");
    }

    /**
     * 好友下线通知
     * @param msg
     */
    public void friendDisOnline(String msg){
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int friend_id = objectNode.get("friend_id").asInt();
        User user = FriendListFrame.friendList.get(friend_id);
        JOptionPane.showMessageDialog(null,"好友"+user.getUserName()+"下线了！");
    }

    /**
     * 接收单聊信息
     * @param msg
     */
    public void receiveSingleMessage(String msg) {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int sender = objectNode.get("sender").asInt();
        String  message = objectNode.get("message").asText();
        String time = objectNode.get("time").asText();
        User user = FriendListFrame.friendList.get(sender);
        //如果有这个聊天窗口的话
        //错误信息
       if(NettyClient.singleChats.containsKey(user)){
           SingleChatFrame singleChatFrame = NettyClient.singleChats.get(user);
           singleChatFrame.showRecvMsg(message,time);
       }else{
           JOptionPane.showMessageDialog(null,"有来自好友"+user.getUserName()+"的消息！");
           //要不直接弹出窗口？
           SingleChatFrame singleChatFrame = new SingleChatFrame(NettyClient.friendListFrame.user,user);
           singleChatFrame.showRecvMsg(message,time);
           NettyClient.singleChats.put(user,singleChatFrame);
       }
    }

    /**
     * 接收群聊消息
     * @param msg
     */
    public void receiveGroupMessage(String msg) {

        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int group_id = objectNode.get("group_id").asInt();
        int sender = objectNode.get("sender").asInt();
        String  message = objectNode.get("message").asText();
        String time = objectNode.get("time").asText();
        ChatGroup chatGroup = FriendListFrame.groups.get(group_id);
        //如果已经有界面的话
        if(NettyClient.groupChats.containsKey(chatGroup)){
            GroupChatFrame groupChatFrame = NettyClient.groupChats.get(chatGroup);
            groupChatFrame.showRecvMsg(FriendListFrame.friendList.get(sender),message,time);
        }else{
            JOptionPane.showMessageDialog(null,"有来自群"+chatGroup+"的消息！");
            //弹出聊天窗口
            GroupChatFrame groupChatFrame = new GroupChatFrame(NettyClient.friendListFrame.user,chatGroup);
            groupChatFrame.showRecvMsg(FriendListFrame.friendList.get(sender),message,time);
            NettyClient.groupChats.put(chatGroup,groupChatFrame);
        }
    }

    /**
     * 接收的类型还是GROUP_MESSAGE
     * 启动一个线程连接服务端并且发送文件给服务端
     */
    public void receivePortAndSendFile(String msg){
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int port = objectNode.get("port").asInt();
        String IP = "localhost";
        String path = objectNode.get("path").asText();
        File file = new File(path);
        //启动子线程
        new SendFileService(IP,port,file).start();
    }

    /**
     * 接收到服务端的端口号，类型为GROUP_MESSAGE_ACK
     * 启动一个线程连接服务端，然后服务端会将文件发送给这个线程
     * @param msg
     */
    public void receiveFile(String msg){
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int port = objectNode.get("port").asInt();
        //接收端接收文件
        int sender = objectNode.get("sender").asInt();
        JOptionPane.showMessageDialog(null,FriendListFrame.friendList.get(sender).getUserName()+"发送文件啦！请注意查收！");

        //启动子线程接收文件
        new ReceiveFileService("localhost",port).start();
    }
}
