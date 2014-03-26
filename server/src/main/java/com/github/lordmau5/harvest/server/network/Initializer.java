package com.github.lordmau5.harvest.server.network;

import com.github.lordmau5.harvest.network.codec.SharedPacketCodec;
import com.github.lordmau5.harvest.network.codec.Varint21FrameDecoder;
import com.github.lordmau5.harvest.network.codec.Varint21FramePrepender;
import com.github.lordmau5.harvest.server.network.handler.InitialHandler;
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

    @Override
    protected void initChannel(Channel ch) throws Exception{
        ChannelPipeline pipe = ch.pipeline();
        pipe.addLast("framePrepender", prepender);
        pipe.addLast("frameDecoder", new Varint21FrameDecoder());
        pipe.addLast("packetCodec", new SharedPacketCodec());
        pipe.addLast("initialHandler", new InitialHandler());
    }
}
