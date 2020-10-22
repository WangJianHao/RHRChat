package com.wjh.service;


import com.wjh.bean.ChatGroup;
import com.wjh.bean.User;
import com.wjh.dao.impl.GroupMapperImpl;
import com.wjh.util.CacheUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class AcceptGroupFileService extends Thread {
    int fromPort;
    int sender;
    int group_id;
    String time;
    public AcceptGroupFileService(int fromPort, int sender, int group_id, String time) {
        this.fromPort = fromPort;
        this.sender = sender;
        this.group_id = group_id;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            //创建ServerSocket实例
            ServerSocket fromServerSocket = new ServerSocket();
            //绑定端口
            fromServerSocket.bind(new InetSocketAddress(fromPort));
            //监听客户端的连接
            Socket fromSocket = fromServerSocket.accept();
            System.out.println("[RHR server]:发送文件线程：发送端连接上服务器");
            //存储到默认目录
            //读取发送端发送的文件流
            DataInputStream dataInputStream = new DataInputStream(fromSocket.getInputStream());
            //获取文件名
            String fileName = dataInputStream.readUTF();
            String path = "C:\\Users\\王鉴豪\\Desktop\\RHRChatServer";
            String filePath = path + File.separator+fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byte[] bytes = new byte[100];
            int len = 0;
            //将文件保存到默认地址
            System.out.println("[RHR server]:将文件保存到默认地址中");
            while ((len = dataInputStream.read(bytes))!= -1){
                fileOutputStream.write(bytes,0,len);
            }
            //关闭资源
            fileOutputStream.close();
            dataInputStream.close();
            System.out.println("[RHR server]:文件保存成功");
            //向数据库中插入记录
            ChatGroup group = new GroupMapperImpl().getGroupById(group_id);
            long user1 = group.getUser1();
            long user2 = group.getUser2();
            long user3 = group.getUser3();
            long user4 = group.getUser4();
            long user5 = group.getUser5();
            FileService fileService = new FileService();
            if(user1 != sender && user1 != -1) {
                fileService.insertRecord(sender, (int) user1, filePath, time, false);
                if(CacheUtil.isOnline((int)user1)) {
                    fileService.sendUnreadMessage((int)user1);
                }
            }
            if(user2 != sender && user2 != -1) {

                fileService.insertRecord(sender, (int) user2, filePath, time, false);
                //转发消息
                if(CacheUtil.isOnline((int)user2)) {
                    fileService.sendUnreadMessage((int)user2);
                }
            }
            if(user3 != sender && user3 != -1) {
                fileService.insertRecord(sender, (int) user3, filePath, time, false);
                if(CacheUtil.isOnline((int)user3)) {
                    fileService.sendUnreadMessage((int)user3);
                }
            }
            if(user4 != sender && user4 != -1) {
                fileService.insertRecord(sender, (int) user4, filePath, time, false);
                if(CacheUtil.isOnline((int)user4)) {
                    fileService.sendUnreadMessage((int)user4);
                }
            }
            if(user5 != sender && user5 != -1) {
                fileService.insertRecord(sender, (int) user5, filePath, time, false);
                if(CacheUtil.isOnline((int)user5)) {
                    fileService.sendUnreadMessage((int)user5);
                }
            }
            System.out.println("[RHR server]:群发消息发送完成");
        }catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
