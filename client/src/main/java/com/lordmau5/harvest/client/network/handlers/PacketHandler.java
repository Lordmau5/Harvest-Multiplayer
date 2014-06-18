package com.lordmau5.harvest.client.network.handlers;

import com.lordmau5.harvest.client.network.NetworkHandler;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:32
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        System.out.println(msg.toString());
        if(msg instanceof PacketPlayerJoin) {
            System.out.println("Player joined! PogChamp");
        }
        msg.process(NetworkHandler.connection);
    }
}