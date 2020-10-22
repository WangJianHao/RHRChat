package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.ChatGroup;
import com.wjh.bean.User;
import com.wjh.constant.MsgType;
import com.wjh.util.JsonUtil;
import com.wjh.util.TimeUtil;
import io.netty.channel.Channel;
import org.omg.CORBA.OBJ_ADAPTER;

import javax.swing.*;
import java.io.File;

public class OnlineService {
    public static Channel channel;

    /**
     * 发送消息的方法
     * @param user
     * @param friend
     * @param msg
     */
    public  void chatWithFriend(User user ,User friend, String msg) {
        if(msg.contains("}")){
            JOptionPane.showMessageDialog(null,"消息中不能包括}");
        }else {
            ObjectNode objectNode = JsonUtil.getObjectNode();
            objectNode.put("msgType", MsgType.SINGLE_MESSAGE);
            objectNode.put("sender", user.getId());
            objectNode.put("receiver", friend.getId());
            objectNode.put("time", TimeUtil.getCurrentTime());
            objectNode.put("message", msg);


            channel.writeAndFlush(objectNode.toString());
        }
    }

    /**
     * 请求更改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     */
    public  void changePassword(String id, String oldPassword, String newPassword) {
        //获取json的node
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("msgType",MsgType.CHANGE_PASSWORD);
        objectNode.put("id",id);
        objectNode.put("oldPassword",oldPassword);
        objectNode.put("newPassword",newPassword);

        channel.writeAndFlush(objectNode.toString());
    }

    /**
     * 群聊天的发送消息
     * @param user
     * @param group
     * @param msg
     */
    public void chatWithGroup(User user, ChatGroup group, String msg) {
        if(msg.contains("}")){
            JOptionPane.showMessageDialog(null,"消息中不能包括}");
        }else {
            //获取封装为json的node
            ObjectNode objectNode = JsonUtil.getObjectNode();
            //填入对应的信息
            objectNode.put("msgType", MsgType.GROUP_MESSAGE);
            objectNode.put("sender", user.getId());
            objectNode.put("group_id", group.getGroupId());
            objectNode.put("time", TimeUtil.getCurrentTime());
            objectNode.put("message", msg);

            //channel将json发送给服务端
            channel.writeAndFlush(objectNode.toString());
        }
    }

    /**
     * 单发文件
     * @param user
     * @param friend
     * @param path
     * @param time
     */
    public void singleFile(User user, User friend, String path, String time) {
        //文件名  后缀名 也要发过去
        //参数校验
        File file = new File(path);
        if(!file.exists() || !file.isFile()){
            //路径不合法
            JOptionPane.showMessageDialog(null,"文件路径不合法！");
            return;
        }
        //先将请求发送给服务端
        ObjectNode objectNode = JsonUtil.getObjectNode();
        //确认请求类型为发送文件
        objectNode.put("msgType",MsgType.SINGLE_FILE);
        objectNode.put("sender",user.getId());
        objectNode.put("receiver",friend.getId());
        objectNode.put("path",path);
        objectNode.put("time",time);

        channel.writeAndFlush(objectNode.toString());
    }

    /**
     * 群发文件
     * @param user
     * @param group
     * @param path
     * @param time
     */
    public void groupFile(User user, ChatGroup group, String path, String time) {
        //文件名  后缀名 也要发过去
        //参数校验
        File file = new File(path);
        if(!file.exists() || !file.isFile()){
            //路径不合法
            JOptionPane.showMessageDialog(null,"文件路径不合法！");
            return;
        }
        //先将请求发送给服务端
        ObjectNode objectNode = JsonUtil.getObjectNode();
        //确认请求类型为发送文件
        objectNode.put("msgType",MsgType.GROUP_FILE);
        objectNode.put("sender",user.getId());
        objectNode.put("group_id",group.getGroupId());
        objectNode.put("path",path);
        objectNode.put("time",time);

        channel.writeAndFlush(objectNode.toString());
    }
}
