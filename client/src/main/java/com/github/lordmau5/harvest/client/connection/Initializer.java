package com.github.lordmau5.harvest.client.connection;

import com.github.lordmau5.harvest.client.connection.handlers.HandshakeHandler;
import com.github.lordmau5.harvest.client.connection.handlers.PacketHandler;
import com.github.lordmau5.harvest.network.codec.HandshakeCodec;
import com.github.lordmau5.harvest.network.codec.Varint21FrameDecoder;
import com.github.lordmau5.harvest.network.codec.Varint21FramePrepender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * No description given
 *
 * @author jk-5
 */
public class Initializer extends ChannelInitializer<Channel> {
    private static final Varint21FramePrepender prepender = new Varint21FramePrepender();
    private static final PacketHandler handler = new PacketHandler();

    private final String host;
    private final int port;

    public Initializer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipe = ch.pipeline();
        pipe.addLast("framePrepender", prepender);
        pipe.addLast("frameDecoder", new Varint21FrameDecoder());
        pipe.addLast("handshakeCodec", new HandshakeCodec());
        pipe.addLast("initialHandler", new HandshakeHandler(this.host, this.port));
        pipe.addLast("packetHandler", handler);
    }
}
