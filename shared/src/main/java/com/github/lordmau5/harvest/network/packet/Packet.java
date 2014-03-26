package com.github.lordmau5.harvest.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class Packet {

    public abstract void encode(ByteBuf buffer);
    public abstract void decode(ByteBuf buffer);
}
