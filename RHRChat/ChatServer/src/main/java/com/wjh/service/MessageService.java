package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.SingleMessage;
import com.wjh.bean.User;
import com.wjh.constant.MsgType;
import com.wjh.dao.impl.MessageMapperImpl;
import com.wjh.util.CacheUtil;
import com.wjh.util.JsonUtil;
import io.netty.channel.Channel;
import org.apache.tomcat.jni.Time;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * 单聊消息表的业务逻辑
 */
public class MessageService {
    private MessageMapperImpl messageMapper =   new MessageMapperImpl();

    /**
     * 单聊的消息
     * @param msg
     * @return
     * @throws SQLException
     */
    public String singleMessage(String msg) throws SQLException {
        //打印日志
        System.out.println("[server]:服务端接收客户端的信息后的业务逻辑");
        ObjectNode objectNode = JsonUtil.getObjectNode(msg);
        int sender = objectNode.get("sender").asInt();
        int receiver = objectNode.get("receiver").asInt();
        String message = objectNode.get("message").asText();
        String time = objectNode.get("time").asText();
        //判断接收方是否在线，如果在线则插入为true，如果不在线则插入为false
        boolean flag = CacheUtil.isOnline(receiver);
        //插入消息记录
        messageMapper.insertRecord(sender, receiver, message, time,flag);
        if(flag){
            System.out.println("[RHR server]:接收方在线");
            Channel channelById = CacheUtil.getChannelById(receiver);
            ObjectNode node = JsonUtil.getObjectNode();
            node.put("msgType",MsgType.SINGLE_MESSAGE_ACK);
            node.put("sender",sender);
            node.put("time",time);
            node.put("message",message);
            if(channelById != null) {
                channelById.writeAndFlush(node.toString());
            }
        }else{
            System.out.println("[server]:接收方不在线");
        }
        return "";
    }

    /**
     * 遍历消息列表获取未读的消息    历史消息暂时不做
     * @param user   刚刚登陆的用户
     */
    public void sendUnreadMessage(User user) throws SQLException {
        //打印日志
        System.out.println("[server]:查询是否有未读的消息");
        //从数据访问层连接数据库查询到所有接收者为user.id的消息，同时也是需要发送者的
       List<SingleMessage> messages = messageMapper.getUnreadMessagesById(user.getId());
       //如果有未读消息的话
       if(messages.size() != 0){
           Channel channelById = CacheUtil.getChannelById((int)user.getId());
           Iterator<SingleMessage> iterator = messages.iterator();
           //遍历未读消息
           while (iterator.hasNext()){
               SingleMessage message = iterator.next();
               ObjectNode objectNode = JsonUtil.getObjectNode();
               objectNode.put("msgType",MsgType.SINGLE_MESSAGE_ACK);
               objectNode.put("sender",message.getSender());
               objectNode.put("time",message.getTime().toString());
               objectNode.put("message",message.getMessage());
               if(channelById != null) {
                   //返回在登录之前
                   channelById.writeAndFlush(objectNode.toString());
                   messageMapper.updateMessageStatus((int)user.getId());
               }
               System.out.println("[server]:未读的单聊消息已发送");
           }
           return;
//           channelById.flush();
       }
        System.out.println("[RHR server]:没有未读的单聊消息！");
    }
}
