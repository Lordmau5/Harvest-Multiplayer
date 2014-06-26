package com.lordmau5.harvest.server.network.handler;

import com.lordmau5.harvest.server.Server;
import com.lordmau5.harvest.server.network.ClientConnection;
import com.lordmau5.harvest.server.network.NetworkServer;
import com.lordmau5.harvest.shared.World;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.network.packet.player.action.PacketPlayerPickupPlace;
import com.lordmau5.harvest.shared.network.packet.player.movement.PacketPlayerMovement;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketRequestPlayers;
import com.lordmau5.harvest.shared.objects.Entity;
import com.lordmau5.harvest.shared.objects.IPickupable;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.newdawn.slick.geom.Rectangle;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:40
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        World world = Server.world;

        if(msg instanceof PacketPlayerMovement) {
            for(ClientConnection con : NetworkServer.connections)
                con.channel().writeAndFlush(msg);
            return;
        }
        else if(msg instanceof PacketRequestPlayers) {
            for(ClientConnection con : NetworkServer.connections)
                if(!con.username().equals(((PacketRequestPlayers) msg).username))
                    ctx.channel().writeAndFlush(new PacketPlayerJoin(con.username()));
            return;
        }
        else if(msg instanceof PacketPlayerPickupPlace) {
            PacketPlayerPickupPlace pkt = (PacketPlayerPickupPlace) msg;

            if(pkt.placeDown) {
                if(world.isEntityInRectangle(new Rectangle(pkt.tile.getX() * 16 + 4, pkt.tile.getY() * 16 + 4, 7, 7)))
                    return;

                world.addEntity(pkt.entityName, pkt.tile.getX(), pkt.tile.getY());
                NetworkServer.sendPacketToAll(new PacketPlayerPickupPlace(pkt.username, pkt.tile, World.entities.get(pkt.entityName)));
                return;
            }

            Entity ent = world.getObjectAtPosition(pkt.tile);
            System.out.println(ent == null);
            if(ent == null || !(ent instanceof IPickupable))
                return;

            world.removeObject(pkt.tile);
            NetworkServer.sendPacketToAll(new PacketPlayerPickupPlace(pkt.username, pkt.tile, null));
            return;
        }
        msg.process(ctx.channel().attr(NetworkServer.clientConnection).get());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NetworkServer.onConnectionClosed(ctx.channel().attr(NetworkServer.clientConnection).get());
        ctx.fireChannelInactive();
    }
}