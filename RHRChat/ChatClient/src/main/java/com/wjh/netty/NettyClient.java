package com.wjh.netty;

import com.wjh.bean.ChatGroup;
import com.wjh.bean.User;
import com.wjh.frame.*;
import com.wjh.service.SendService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Hashtable;

public class NettyClient {
    public static RegisterFrame registerFrame;
    public static LoginFrame loginFrame;
    public static FriendListFrame friendListFrame;
    public static ForgetPasswordFrame forgetPasswordFrame;
    public static Hashtable<User,SingleChatFrame> singleChats = new Hashtable<>(10);
    public static Hashtable<ChatGroup,GroupChatFrame> groupChats = new Hashtable<>(10);
    public static ChangePasswordFrame changePasswordFrame ;

    /**
     * 启动客户端的方法
     * @param ip
     * @param port
     */
    public static void start(String ip,int port) {
        //创建事件循环组
        NioEventLoopGroup work = new NioEventLoopGroup();

        //创建启动辅助类
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                //绑定事件循环组
                .group(work)
                //指定时间循环组的channel类型
                .channel(NioSocketChannel.class)
                //自定义channelHandler，具体业务逻辑处理
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            System.out.println("[RHR client]:客户端已启动！");
            SendService.channel = channel;
//            channel.writeAndFlush("Hello Server!I'm Client.");
            loginFrame = new LoginFrame();

            //同步关闭
            channel.closeFuture().sync();

            //直接关闭
//            channel.close().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
        }
    }
}
