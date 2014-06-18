package com.lordmau5.harvest.server.network.handler;

import com.lordmau5.harvest.server.network.NetworkServer;
import com.lordmau5.harvest.shared.network.packet.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:40
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        msg.process(ctx.channel().attr(NetworkServer.clientConnection).get());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NetworkServer.onConnectionClosed(ctx.channel().attr(NetworkServer.clientConnection).get());
        ctx.fireChannelInactive();
    }
}