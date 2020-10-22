package com.wjh.netty;

import com.wjh.bean.User;
import com.wjh.controller.NettyController;
import com.wjh.service.FriendService;
import com.wjh.util.CacheUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }

    //真正接收客户端的数据并处理
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("[RHR server]:"+msg);
        //客户端发送过来的消息交给Controller
        String recvMsg = NettyController.process(msg.toString(),ctx);
        if(recvMsg != null && !recvMsg.equals("")) {
            System.out.println("[RHR server]:"+recvMsg);
            ctx.channel().writeAndFlush(recvMsg);
        }

    }

    //用户上线，执行相关操作
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[RHR server]:"+ctx.channel().remoteAddress()+"上线了。。。。。。");
    }

    //用户下线，执行相关操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //用户下线时，可以知道是哪个ip下线，所以要根据ip拿到用户的实体类
        //给好友通知
        User user = CacheUtil.getUserByChannel(ctx.channel());
        if(user != null) {
            new FriendService(user).renewFriendDisOnline();
        }
        //要清除缓存
        CacheUtil.remove(ctx.channel());

        System.out.println("[RHR server]:"+ctx.channel().remoteAddress()+"下线了。。。。。。");
    }
}
