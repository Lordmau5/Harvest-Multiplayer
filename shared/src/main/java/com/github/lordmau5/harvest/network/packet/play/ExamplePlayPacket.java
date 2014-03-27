package com.github.lordmau5.harvest.network.packet.play;

import com.github.lordmau5.harvest.network.ConnectionContext;
import com.github.lordmau5.harvest.network.packet.Packet;
import io.netty.buffer.ByteBuf;

/**
 * No description given
 *
 * @author jk-5
 */
public class ExamplePlayPacket extends Packet {

    @Override
    public void encode(ByteBuf buffer){

    }

    @Override
    public void decode(ByteBuf buffer){

    }

    @Override
    public void process(ConnectionContext ctx){
        System.out.println("Received example play packet!");
        //Send a packet back:
        //ctx.channel().writeAndFlush(new OtherPacket());
    }
}
