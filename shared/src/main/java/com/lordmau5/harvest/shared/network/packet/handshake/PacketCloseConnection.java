package com.lordmau5.harvest.shared.network.packet.handshake;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.PacketUtils;
import com.lordmau5.harvest.shared.network.packet.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:30
 */
public class PacketCloseConnection extends Packet {
    public String reason = "N/A";

    public PacketCloseConnection() {

    }

    public PacketCloseConnection(String reason) {
        this.reason = reason;
    }

    @Override
    public void encode(ByteBuf buffer) {
        PacketUtils.writeString(this.reason, buffer);
    }

    @Override
    public void decode(ByteBuf buffer) {
        this.reason = PacketUtils.readString(buffer);
    }

    @Override
    public void process(ConnectionContext ctx) {
        System.out.println("Closed. Reason: " + this.reason);
        ctx.channel().close();
    }
}