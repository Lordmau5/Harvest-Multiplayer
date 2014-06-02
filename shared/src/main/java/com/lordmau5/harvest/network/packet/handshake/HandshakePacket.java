package com.lordmau5.harvest.network.packet.handshake;

import com.lordmau5.harvest.network.ConnectionContext;
import com.lordmau5.harvest.network.packet.Packet;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class HandshakePacket extends Packet {
    @Override
    public final void process(ConnectionContext ctx) {
        //Handshake packets don't need this
    }
}
