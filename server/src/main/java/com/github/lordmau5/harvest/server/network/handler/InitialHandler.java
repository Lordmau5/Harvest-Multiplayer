package com.github.lordmau5.harvest.server.network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * No description given
 *
 * @author jk-5
 */
public class InitialHandler extends ChannelInboundHandlerAdapter {

    //TODO: listen for connections and build ConnectionHandlers

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("Incoming connection: " + ctx.channel().remoteAddress().toString());
    }
}
