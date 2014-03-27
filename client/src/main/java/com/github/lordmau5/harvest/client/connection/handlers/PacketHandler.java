package com.github.lordmau5.harvest.client.connection.handlers;

import com.github.lordmau5.harvest.client.connection.NetworkHandler;
import com.github.lordmau5.harvest.network.packet.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * No description given
 *
 * @author jk-5
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception{
        msg.process(NetworkHandler.connection);
    }
}
