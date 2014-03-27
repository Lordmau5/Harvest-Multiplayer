package com.github.lordmau5.harvest.network.codec;

import com.github.lordmau5.harvest.network.packet.handshake.PacketClientHandshake;
import com.github.lordmau5.harvest.network.packet.handshake.PacketCloseConnection;
import com.github.lordmau5.harvest.network.packet.handshake.PacketServerHandshake;

/**
 * No description given
 *
 * @author jk-5
 */
public class HandshakeCodec extends PacketCodec {

    public HandshakeCodec(){
        this.registerPacket(1, PacketClientHandshake.class);
        this.registerPacket(2, PacketServerHandshake.class);
        this.registerPacket(3, PacketCloseConnection.class);
    }
}
