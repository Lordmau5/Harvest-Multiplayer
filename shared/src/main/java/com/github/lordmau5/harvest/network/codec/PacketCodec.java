package com.github.lordmau5.harvest.network.codec;

import com.github.lordmau5.harvest.network.PacketUtils;
import com.github.lordmau5.harvest.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class PacketCodec extends ByteToMessageCodec<Packet> {
    private Map<Integer, Class<? extends Packet>> idToClass = new HashMap<>();
    private Map<Class<? extends Packet>, Integer> classToId = new HashMap<>();

    public PacketCodec registerPacket(int id, Class<? extends Packet> packet) {
        this.idToClass.put(id, packet);
        this.classToId.put(packet, id);
        return this;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        Class<? extends Packet> cl = msg.getClass();
        if (!this.classToId.containsKey(cl)) {
            throw new UnsupportedOperationException("Trying to send an unregistered packet (" + cl.getSimpleName() + ")");
        }
        int id = this.classToId.get(cl);
        PacketUtils.writeVarInt(id, out);
        msg.encode(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int id = PacketUtils.readVarInt(in);
        if (!this.idToClass.containsKey(id)) {
            throw new UnsupportedOperationException("Received an unknown packet (id: " + id + ")");
        }
        Packet packet = this.idToClass.get(id).newInstance();
        packet.decode(in);
        out.add(packet);
    }
}
