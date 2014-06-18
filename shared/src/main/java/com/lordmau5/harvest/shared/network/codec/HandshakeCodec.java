package com.lordmau5.harvest.shared.network.codec;

import com.lordmau5.harvest.shared.network.packet.handshake.PacketClientHandshake;
import com.lordmau5.harvest.shared.network.packet.handshake.PacketCloseConnection;
import com.lordmau5.harvest.shared.network.packet.handshake.PacketServerHandshake;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:28
 */
public class HandshakeCodec extends PacketCodec {
    public HandshakeCodec() {
        this.registerPacket(1, PacketClientHandshake.class);
        this.registerPacket(2, PacketServerHandshake.class);
        this.registerPacket(3, PacketCloseConnection.class);
    }
}