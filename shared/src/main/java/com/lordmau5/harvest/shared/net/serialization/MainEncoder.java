package com.lordmau5.harvest.shared.net.serialization;

import com.lordmau5.harvest.shared.net.packet.PacketBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;

public class MainEncoder extends MessageToByteEncoder<PacketBase> {
    private final PacketSerializer packetSerializer;

    public MainEncoder() {
        packetSerializer = new PacketSerializer();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, PacketBase msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        packetSerializer.serialize(stream, msg);
        out.writeBytes(stream.toByteArray());
    }
}
