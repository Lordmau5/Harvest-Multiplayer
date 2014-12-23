package com.lordmau5.harvest.shared.net.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MainDecoder extends ByteToMessageDecoder {
    private final PacketSerializer packetSerializer;

    public MainDecoder() {
        packetSerializer = new PacketSerializer();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4)
            return;
        out.add(packetSerializer.deserialize(in));
    }
}

