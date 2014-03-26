package com.github.lordmau5.harvest.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * No description given
 *
 * @author jk-5
 */
public class NetworkServer {

    private static EventLoopGroup boss;
    private static EventLoopGroup worker;

    public static void start(String bindHost, int port){
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap().group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new Initializer()).localAddress(bindHost, port);
        b.bind().channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception{
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }).syncUninterruptibly(); //TODO: remove this sync!
    }
}
