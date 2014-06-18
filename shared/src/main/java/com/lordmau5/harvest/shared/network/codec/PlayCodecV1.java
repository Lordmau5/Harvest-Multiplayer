package com.lordmau5.harvest.shared.network.codec;

import com.lordmau5.harvest.shared.network.packet.handshake.PacketCloseConnection;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:46
 */
public class PlayCodecV1 extends PacketCodec {
    public PlayCodecV1() {
        //TODO: add gameplay packets
        this.registerPacket(1, PacketCloseConnection.class);
        this.registerPacket(2, PacketPlayerJoin.class);
    }
}