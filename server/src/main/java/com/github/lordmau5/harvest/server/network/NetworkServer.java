package com.github.lordmau5.harvest.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class NetworkServer {
    public static final List<ClientConnection> connections = new ArrayList<>();
    public static final AttributeKey<ClientConnection> clientConnection = AttributeKey.valueOf("clientConnection");
    public static final byte PROTOCOL_VERSION = 1;
    private static EventLoopGroup boss;
    private static EventLoopGroup worker;

    public static void start(String bindHost, int port) {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap().group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new Initializer()).localAddress(bindHost, port);
        bootstrap.bind().channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //Close the worker threads when the ServerChannel closes
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }).syncUninterruptibly(); //TODO: remove this sync!
    }

    public static void onConnectionOpened(ClientConnection connection) {
        connections.add(connection);
        System.out.println("Player " + connection.username() + "(" + connection.channel().remoteAddress().toString() + ") logged in!");
    }

    public static void onConnectionClosed(ClientConnection connection) {
        connections.remove(connection);
        System.out.println("Player " + connection.username() + " logged out!");
    }
}
