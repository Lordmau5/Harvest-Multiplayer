package com.lordmau5.harvest.shared.net.channel;

import com.lordmau5.harvest.shared.net.serialization.MainDecoder;
import com.lordmau5.harvest.shared.net.serialization.MainEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MainChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ChannelHandler authorizationHandler;
    private final ChannelHandler logicHandler;
    private final MainDecoder mainDecoder;
    private final MainEncoder mainEncoder;

    public MainChannelInitializer(ChannelHandler authorizationHandler, ChannelHandler logicHandler) {
        mainDecoder = new MainDecoder();
        mainEncoder = new MainEncoder();
        this.authorizationHandler = authorizationHandler;
        this.logicHandler = logicHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", mainDecoder);
        pipeline.addLast("encoder", mainEncoder);
        pipeline.addLast("authorization", authorizationHandler);
        pipeline.addLast("logic", logicHandler);
    }
}
