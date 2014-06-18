package com.lordmau5.harvest.client.network;

import com.lordmau5.harvest.client.network.handlers.HandshakeHandler;
import com.lordmau5.harvest.client.network.handlers.PacketHandler;
import com.lordmau5.harvest.shared.network.codec.HandshakeCodec;
import com.lordmau5.harvest.shared.network.codec.Varint21FrameDecoder;
import com.lordmau5.harvest.shared.network.codec.Varint21FramePrepender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:26
 */
public class ClientInitializer extends ChannelInitializer {

    private static final Varint21FramePrepender prepender = new Varint21FramePrepender();
    private static final PacketHandler handler = new PacketHandler();

    private final String host;
    private final int port;

    public ClientInitializer(String host, int port) {
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
