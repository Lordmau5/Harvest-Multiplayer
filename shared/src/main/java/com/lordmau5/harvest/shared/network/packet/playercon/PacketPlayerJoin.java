package com.lordmau5.harvest.shared.network.packet.playercon;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.PacketUtils;
import com.lordmau5.harvest.shared.network.packet.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:48
 */
public class PacketPlayerJoin extends Packet {
    public String username = "NULL";

    public PacketPlayerJoin() {

    }

    public PacketPlayerJoin(String username) {
        this.username = username;
    }

    @Override
    public void encode(ByteBuf buffer) {
        PacketUtils.writeString(this.username, buffer);
    }

    @Override
    public void decode(ByteBuf buffer) {
        this.username = PacketUtils.readString(buffer);
    }

    @Override
    public void process(ConnectionContext ctx) {

    }
}
