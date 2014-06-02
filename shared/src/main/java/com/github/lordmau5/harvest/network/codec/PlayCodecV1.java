package com.github.lordmau5.harvest.network.codec;

import com.github.lordmau5.harvest.network.packet.handshake.PacketCloseConnection;
import com.github.lordmau5.harvest.network.packet.play.ExamplePlayPacket;

/**
 * No description given
 *
 * @author jk-5
 */
public class PlayCodecV1 extends PacketCodec {
    public PlayCodecV1() {
        //TODO: add gameplay packets
        this.registerPacket(1, ExamplePlayPacket.class);
        this.registerPacket(2, PacketCloseConnection.class);
    }
}
