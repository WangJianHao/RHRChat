package com.wjh.service;

import sun.reflect.generics.scope.Scope;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class TransferFileService extends Thread {
    int fromPort;
    int toPort;
    int sender;
    int receiver;
    String time;
    public TransferFileService(int fromPort,int toPort,int sender,int receiver,String time){
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }
    public TransferFileService(int fromPort,int toPort){
        this.fromPort = fromPort;
        this.toPort = toPort;
    }

    @Override
    public void run() {
        if(toPort == 0){
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
                new FileService().insertRecord(sender,receiver,filePath,time,false);
            }catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                //创建ServerSocket实例
                ServerSocket fromServerSocket = new ServerSocket();
                ServerSocket toServerSocket = new ServerSocket();
                //绑定端口
                fromServerSocket.bind(new InetSocketAddress(fromPort));
                toServerSocket.bind(new InetSocketAddress(toPort));
                //监听客户端的连接
                Socket fromSocket = fromServerSocket.accept();
                Socket toSocket = toServerSocket.accept();
                System.out.println("[RHR server]:发送文件线程：发送端和接收端连接上服务器");
                //进行读写操作
                //将发送端的fromSocket文件流转发给接收端的toSocket文件流
                //读取发送方的文件流
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fromSocket.getInputStream());
                //将文件流写入接收端的流
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(toSocket.getOutputStream());
                byte[] bytes = new byte[100];
                int len = 0;
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                //关闭资源
                bufferedOutputStream.close();
                bufferedInputStream.close();
                System.out.println("[RHR server]:文件发送完成！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
