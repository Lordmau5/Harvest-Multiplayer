package com.github.lordmau5.harvest.network.packet.handshake;

import io.netty.buffer.ByteBuf;

/**
 * No description given
 *
 * @author jk-5
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
