package com.lordmau5.harvest.shared.network.packet;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:23
 */
public abstract class Packet {
    public abstract void encode(ByteBuf buffer);

    public abstract void decode(ByteBuf buffer);

    public abstract void process(ConnectionContext ctx);
}
