package com.lordmau5.harvest.shared.network.codec;

import com.lordmau5.harvest.shared.network.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:28
 */
@ChannelHandler.Sharable
public class Varint21FramePrepender extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bodyLength = msg.readableBytes();
        int headerLength = PacketUtils.varIntSize(bodyLength);
        out.ensureWritable(headerLength + bodyLength);
        PacketUtils.writeVarInt(bodyLength, out);
        out.writeBytes(msg);
    }
}
