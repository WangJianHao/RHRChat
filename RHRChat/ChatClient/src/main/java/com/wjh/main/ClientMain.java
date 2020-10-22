package com.wjh.main;

import com.wjh.netty.NettyClient;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class ClientMain {

    public static void main(String[] args) {
        NettyClient.start("127.0.0.1",8889);


    }
}
