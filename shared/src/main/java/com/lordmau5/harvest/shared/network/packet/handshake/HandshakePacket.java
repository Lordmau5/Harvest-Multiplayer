package com.lordmau5.harvest.shared.network.packet.handshake;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.packet.Packet;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:29
 */
public abstract class HandshakePacket extends Packet {
    @Override
    public final void process(ConnectionContext ctx) {
        //Handshake packets don't need this
    }
}