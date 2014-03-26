package com.github.lordmau5.harvest.network.codec;

import com.github.lordmau5.harvest.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class PacketCodec extends ByteToMessageCodec<Packet> {

    private Map<Byte, Class<? extends Packet>> idToClass = new IdentityHashMap<Byte, Class<? extends Packet>>();
    private Map<Class<? extends Packet>, Byte> classToId = new IdentityHashMap<Class<? extends Packet>, Byte>();

    public PacketCodec registerPacket(int id, Class<? extends Packet> packet){
        this.idToClass.put((byte) id, packet);
        this.classToId.put(packet, (byte) id);
        return this;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception{
        Class<? extends Packet> cl = msg.getClass();
        if(!this.classToId.containsKey(cl)){
            throw new UnsupportedOperationException("Trying to send an unregistered packet (" + cl.getSimpleName() + ")");
        }
        byte id = this.classToId.get(cl);
        out.writeByte(id);
        msg.encode(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception{
        byte id = in.readByte();
        if(!this.idToClass.containsKey(id)){
            throw new UnsupportedOperationException("Received an unknown packet (id: " + id + ")");
        }
        Packet packet = this.idToClass.get(id).newInstance();
        packet.decode(in);
        out.add(packet);
    }
}
