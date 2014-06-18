package com.lordmau5.harvest.server.network;

import com.lordmau5.harvest.server.network.handler.HandshakeHandler;
import com.lordmau5.harvest.server.network.handler.PacketHandler;
import com.lordmau5.harvest.shared.network.codec.HandshakeCodec;
import com.lordmau5.harvest.shared.network.codec.Varint21FrameDecoder;
import com.lordmau5.harvest.shared.network.codec.Varint21FramePrepender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:38
 */
public class ServerInitializer extends ChannelInitializer {
    //The prepender is Sharable and threadsafe, so reuse it for each connection
    private static final Varint21FramePrepender prepender = new Varint21FramePrepender();
    private static final PacketHandler handler = new PacketHandler();

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipe = channel.pipeline();
        pipe.addLast("framePrepender", prepender);
        pipe.addLast("frameDecoder", new Varint21FrameDecoder());
        pipe.addLast("handshakeCodec", new HandshakeCodec());
        pipe.addLast("initialHandler", new HandshakeHandler());
        pipe.addLast("packetHandler", handler);
    }
}