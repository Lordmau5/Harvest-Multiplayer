package com.lordmau5.harvest.shared.network.packet.handshake;

import com.lordmau5.harvest.shared.network.PacketUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:30
 */
public class PacketClientHandshake extends HandshakePacket {
    public String host;
    public int port;
    public byte protocolVersion;
    public String username;

    public PacketClientHandshake() {

    }

    public PacketClientHandshake(String host, int port, byte protocolVersion, String username) {
        this.host = host;
        this.port = port;
        this.protocolVersion = protocolVersion;
        this.username = username;
    }

    @Override
    public void encode(ByteBuf buffer) {
        PacketUtils.writeString(this.host, buffer);
        PacketUtils.writeVarInt(this.port, buffer);
        buffer.writeByte(this.protocolVersion);
        PacketUtils.writeString(this.username, buffer);
    }

    @Override
    public void decode(ByteBuf buffer) {
        this.host = PacketUtils.readString(buffer);
        this.port = PacketUtils.readVarInt(buffer);
        this.protocolVersion = buffer.readByte();
        this.username = PacketUtils.readString(buffer);
    }
}