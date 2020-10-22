package com.wjh.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SendUnreadService extends Thread {
    int toPort;
    String path;
    public SendUnreadService(int toPort,String path){
        this.toPort = toPort;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            //创建ServerSocket实例
            ServerSocket toServerSocket = new ServerSocket();
            //绑定端口
            toServerSocket.bind(new InetSocketAddress(toPort));
            //监听客户端的连接
            Socket toSocket = toServerSocket.accept();
            System.out.println("[RHR server]:发送文件线程：接收端连接上服务器");
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(toSocket.getOutputStream());
            dataOutputStream.writeUTF(file.getName());
            //写文件内容
            byte[] bytes = new byte[100];
            int len = 0;
            while ((len = fileInputStream.read(bytes))!= -1){
                dataOutputStream.write(bytes,0,len);
            }
            //关闭资源
            dataOutputStream.close();
            fileInputStream.close();
            System.out.println("[RHR server]:文件"+file.getName()+"发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
