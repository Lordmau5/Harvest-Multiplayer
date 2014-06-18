package com.lordmau5.harvest.shared.network.packet.handshake;

import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:31
 */
public class PacketServerHandshake extends HandshakePacket {
    public byte protocolVersion;

    public PacketServerHandshake() {

    }

    public PacketServerHandshake(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeByte(this.protocolVersion);
    }

    @Override
    public void decode(ByteBuf buffer) {
        this.protocolVersion = buffer.readByte();
    }
}