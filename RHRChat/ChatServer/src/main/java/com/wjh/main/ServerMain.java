package com.wjh.main;

import com.wjh.netty.NettyServer;

public class ServerMain {
    public static void main(String[] args) {
        NettyServer.start(8889);
    }
}
