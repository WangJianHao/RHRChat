package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.SingleFile;
import com.wjh.bean.User;
import com.wjh.constant.MsgType;
import com.wjh.dao.impl.SingleFileMapperImpl;
import com.wjh.util.CacheUtil;
import com.wjh.util.JsonUtil;
import com.wjh.util.PortUtil;
import io.netty.channel.Channel;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class FileService {
    private SingleFileMapperImpl singleFileMapper = new SingleFileMapperImpl();

    public void insertRecord(int sender,int receiver,String path,String time,boolean status) throws SQLException {
        int i = singleFileMapper.insertFileRecord(sender, receiver, path, time, status);
        if(i == 1){
            System.out.println("[RHR server]:插入文件记录成功");
        }
    }

    public void sendUnreadMessage(int userId) throws SQLException {
        List<SingleFile> singleFiles = singleFileMapper.queryUnreadMessage(userId);
        Iterator<SingleFile> iterator = singleFiles.iterator();
        while (iterator.hasNext()){
            System.out.println("[RHR server]:文件接收方登录");
            SingleFile singleFile = iterator.next();
            //接收端在线
            Channel channelById = CacheUtil.getChannelById(userId);
            //给接收方分配一个端口
            int toPort = PortUtil.getFreePort();
            String path = singleFile.getPath();
            new SendUnreadService(toPort,path).start();
            System.out.println("[RHR server]:服务端子线程启动！");
            //给接收方发送Json数据
            ObjectNode receiverNode = JsonUtil.getObjectNode();
            receiverNode.put("msgType",MsgType.SINGLE_FILE_ACK);
            receiverNode.put("sender",singleFile.getSender());
            receiverNode.put("port",toPort);
            //给接收端发送端口
            if(channelById != null) {
                channelById.writeAndFlush(receiverNode.toString());
            }
        }
    }
}
