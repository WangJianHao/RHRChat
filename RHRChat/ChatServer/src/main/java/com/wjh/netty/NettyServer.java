package com.wjh.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {

    /**
     * 服务器启动特定端口
     * @param port
     */
    public static void start(int port) {
        //创建两个事件循环组,boss,worker事件循环组
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //设置5个工作线程
        NioEventLoopGroup worker = new NioEventLoopGroup(5);

        //创建启动辅助类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                //将事件循环组配置到启动辅助类中
                .group(boss,worker)
                //指定主事件循环组处理的channel类型
                .channel(NioServerSocketChannel.class)
                //自定义channelHandler，具体业务逻辑处理
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //获取pipeline容器
                        ChannelPipeline pipeline = ch.pipeline();

                        //配置编码解码，具体业务具体处理，此处是字符串
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        //添加channelHandler   自定义channelHandler
                        pipeline.addLast(new ServerHandler());
                    }
                });
        //同步启动服务端
        //启动服务端通过sync()同步阻塞，直至服务器的完全启动完成，否则会阻塞
        //服务器启动返回了ChannelFuture类
        try {
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            System.out.println("[server]:RHR聊天项目服务端已启动");
            //关闭channel
            //关闭操作会阻塞，直至通道完全关闭
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
