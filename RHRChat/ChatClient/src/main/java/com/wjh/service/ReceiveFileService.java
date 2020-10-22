package com.wjh.service;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ReceiveFileService extends Thread {
    String IP;
    int port;
    public ReceiveFileService(String IP,int port){
        this.IP = IP;
        this.port = port;
    }

    @Override
    public void run() {
        //创建Socket实例连接服务端
        Socket socket = new Socket();

        try {
            //连接服务器
            socket.connect(new InetSocketAddress(IP,port));
            //读取服务端转发的消息
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            //获取文件名
            String fileName = dataInputStream.readUTF();
            //保存路径
            String path = "C:\\Users\\王鉴豪\\Desktop\\RHRChat";
            String filePath = path + File.separator+fileName;

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byte[] bytes = new byte[100];
            int len = 0;
            //将文件保存到默认地址
            System.out.println("[RHR client]:将文件保存到默认地址中");
            while ((len = dataInputStream.read(bytes))!= -1){
                fileOutputStream.write(bytes,0,len);
            }
            //关闭资源
            fileOutputStream.close();
            dataInputStream.close();
            JOptionPane.showMessageDialog(null,"[RHR client]:文件接收完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
