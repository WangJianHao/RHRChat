package com.wjh.service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SendFileService extends Thread {
    private String IP;
    private int port;
    private File file;

    public SendFileService(String IP,int port,File file){
        this.IP = IP;
        this.port = port;
        this.file = file;
    }

    @Override
    public void run() {
        //创建Socket实例
        Socket socket = new Socket();
        try {
            //连接服务端
            socket.connect(new InetSocketAddress(IP,port));
            System.out.println("[RHR client]:子线程连接到服务端");
            //进行写操作
            //发送包含文件名和文件内容  读文件流
            FileInputStream fileInputStream = new FileInputStream(file);
            //写入网络中
            //发送流
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //写文件名
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
            System.out.println("[RHR client]:文件"+file.getName()+"发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
