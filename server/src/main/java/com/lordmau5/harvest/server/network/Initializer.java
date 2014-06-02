package com.lordmau5.harvest.server.network;

import com.lordmau5.harvest.network.codec.HandshakeCodec;
import com.lordmau5.harvest.network.codec.Varint21FrameDecoder;
import com.lordmau5.harvest.network.codec.Varint21FramePrepender;
import com.lordmau5.harvest.server.network.handler.HandshakeHandler;
import com.lordmau5.harvest.server.network.handler.PacketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * No description given
 *
 * @author jk-5
 */
public class Initializer extends ChannelInitializer {
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
