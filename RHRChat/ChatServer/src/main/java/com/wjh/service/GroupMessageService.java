package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.ChatGroup;
import com.wjh.bean.GroupMessage;
import com.wjh.bean.User;
import com.wjh.constant.MsgType;
import com.wjh.dao.impl.GroupMapperImpl;
import com.wjh.dao.impl.GroupMessageMapperImpl;
import com.wjh.util.CacheUtil;
import com.wjh.util.JsonUtil;
import io.netty.channel.Channel;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupMessageService {
    private GroupMessageMapperImpl groupMessageMapper = new GroupMessageMapperImpl();
    /**
     * 本方法用于接收本用户所在的群的未读消息
     * @param user
     * @throws SQLException
     */
    public void sendUnreadMessage(User user)throws SQLException {
        System.out.println("[server]:查询数据库是否有未接收的群聊消息");
        //拿到封装Json消息的node
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("msgType",MsgType.GROUP_MESSAGE_ACK);
        GroupMapperImpl groupMapper = new GroupMapperImpl();
        HashMap<Integer, ChatGroup> groups = groupMapper.getGroupByUser((int) user.getId());
        //根据group_id获取消息
        Iterator<Map.Entry<Integer, ChatGroup>> iterator = groups.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, ChatGroup> next = iterator.next();
            int groupId = next.getKey();
            ChatGroup group = next.getValue();
            //根据groupId查询群消息列表查找是否有未读消息,接收者为user.id并且status为false
            List<GroupMessage> groupMessages = groupMessageMapper.queryUnreadMessageByGroupAndUser(groupId, user.getId());
            Iterator<GroupMessage> iterator1 = groupMessages.iterator();
            while (iterator1.hasNext()){
                GroupMessage groupMessage = iterator1.next();
                ObjectNode groupMessageNode = JsonUtil.getObjectNode();
                groupMessageNode.put("msgType",MsgType.GROUP_MESSAGE_ACK);
                groupMessageNode.put("group_id",groupMessage.getGroupId());
                groupMessageNode.put("sender",groupMessage.getSender());
                groupMessageNode.put("time",groupMessage.getTime().toString());
                groupMessageNode.put("message",groupMessage.getMessage());
                Channel channelById = CacheUtil.getChannelById((int) user.getId());
                if(channelById != null) {
                    channelById.writeAndFlush(groupMessageNode.toString());
                }
            }
            System.out.println("[RHR server]:未读的群聊消息已发送");

        }
    }

    /**
     * 客户端发送群聊消息的业务逻辑处理
     * @param msg
     * @return
     */
    public String groupMessage(String msg) throws SQLException {
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int sender = objectNode.get("sender").asInt();
        int group_id = objectNode.get("group_id").asInt();
        String message = objectNode.get("message").asText();
        String time = objectNode.get("time").asText();
        //打印日志
        System.out.println("[server]:根据群号获取群消息");
        ChatGroup group = new GroupMapperImpl().getGroupById(group_id);
        System.out.println("[server]:已经获取到群信息");
        //挨个插入接收者分别为user1 user2 user3 user4 user5
        sendGroupMessage(group.getUser1(),sender,group_id,message,time);
        sendGroupMessage(group.getUser2(),sender,group_id,message,time);
        sendGroupMessage(group.getUser3(),sender,group_id,message,time);
        sendGroupMessage(group.getUser4(),sender,group_id,message,time);
        sendGroupMessage(group.getUser5(),sender,group_id,message,time);
        System.out.println("[server]:群消息发送完成");
        return "";
    }

    private void sendGroupMessage(long user,int sender,int group_id,String message,String time) throws SQLException {
        if(user != sender && user != -1) {
            if(CacheUtil.isOnline((int)user)){
                //如果在线的话，先插入记录然后再将message返回给他
                System.out.println("[server]:找到对应的channel，并将群消息转发给群成员");
                groupMessageMapper.insertMessageRecord(group_id, sender, user,message,time,true);
                Channel channelById = CacheUtil.getChannelById((int) user);
                ObjectNode user1 = JsonUtil.getObjectNode();
                //封装群聊消息的json信息
                user1.put("msgType",MsgType.GROUP_MESSAGE_ACK);
                user1.put("group_id",group_id);
                user1.put("sender",sender);
                user1.put("time",time);
                user1.put("message",message);
                if(channelById != null) {
                    channelById.writeAndFlush(user1.toString());
                }
            }else{
                //未在线的话插入离线消息记录
                groupMessageMapper.insertMessageRecord(group_id, sender, user,message,time,false);
            }
        }
    }
}
