package com.wjh.netty;

import com.wjh.controller.ClientController;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler {
    StringBuffer partMsg = new StringBuffer();
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg.toString().endsWith("}")) {
            partMsg.append(msg.toString());
            String str = partMsg.toString();
            while (str.endsWith("}")) {
                System.out.println(str.substring(0, str.indexOf("}") + 1));
                ClientController.process(str);
                if(str.indexOf("}")+1 > str.length()){
                    break;
                }
                str = str.substring(str.indexOf("}") + 1);
            }
            partMsg = new StringBuffer();
        }else{
            partMsg.append(msg.toString());
        }
    }
}
