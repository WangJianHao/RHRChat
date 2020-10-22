package com.wjh.util;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Random;

public class PortUtil {
    //已被占用端口集合
    private static HashSet<Integer> busyPorts = new HashSet<>();

    public static synchronized int getFreePort(){
        System.out.println("[RHR server]:分配空闲端口中");
        Random random = new Random();
        while (true){
            //随机生成端口，取值范围在5001-65535  左闭右开
            int port = random.nextInt(60535) + 5001;
            if(busyPorts.contains(port)) {
                //端口已被占用，则再重新产生端口
                continue;
            }
            try{
                //用new DatagramSocket(port)能测试本地主机
                //如果port被绑定，则会被IOException捕获，未被绑定则可以使用
                new DatagramSocket(port);
                //未报异常就可以直接使用，并将该端口加入已被使用集合

                busyPorts.add(port);
                return port;
            }catch (SocketException e){
//                System.out.println("[RHR server]:端口被占用");
            }
        }
    }

    public static synchronized void closePort(int port){
        if(busyPorts.contains(port)){
            System.out.println("关闭端口");
            busyPorts.remove(port);
        }
    }
}
