package com.lordmau5.harvest.client.net;

import com.lordmau5.harvest.client.net.listener.ConnectionClosedListener;
import com.lordmau5.harvest.client.net.listener.ConnectionEstablishedListener;
import com.lordmau5.harvest.shared.net.channel.MainChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HarvestClient {
    private final String host;
    private final int port;
    private final MainChannelInitializer mainChannelInitializer;
    private final EventLoopGroup worker;
    private Channel channel;
    private ConnectionEstablishedListener connectionEstablishedListener;
    private ConnectionClosedListener connectionClosedListener;

    public HarvestClient(String host, int port) {
        this.host = host;
        this.port = port;

        mainChannelInitializer = new MainChannelInitializer(
                new ClientAuthorizationHandler(), new ClientLogicHandler()
        );
        worker = new NioEventLoopGroup();

        connectionEstablishedListener = new ConnectionEstablishedListener();
        connectionClosedListener = new ConnectionClosedListener();
    }

    public void connect() {
        Bootstrap bootstrap = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(mainChannelInitializer);
        channel = bootstrap
                .connect(host, port)
                .addListener(connectionEstablishedListener)
                .channel();
        channel.closeFuture()
                .addListener(connectionClosedListener);
    }
}
