package com.github.lordmau5.harvest.network.packet.handshake;

import com.github.lordmau5.harvest.network.PacketUtils;
import io.netty.buffer.ByteBuf;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketCloseConnection extends HandshakePacket {

    public String reason;

    public PacketCloseConnection(){}
    public PacketCloseConnection(String reason){
        this.reason = reason;
    }

    @Override
    public void encode(ByteBuf buffer){
        PacketUtils.writeString(this.reason, buffer);
    }

    @Override
    public void decode(ByteBuf buffer){
        this.reason = PacketUtils.readString(buffer);
    }
}
