package com.lordmau5.harvest.client.network.handlers;

import com.lordmau5.harvest.client.Client;
import com.lordmau5.harvest.client.network.NetworkHandler;
import com.lordmau5.harvest.shared.World;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.network.packet.player.action.PacketPlayerPickupPlace;
import com.lordmau5.harvest.shared.network.packet.player.movement.PacketPlayerMovement;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerLeave;
import com.lordmau5.harvest.shared.network.packet.world.PacketInitWorld;
import com.lordmau5.harvest.shared.objects.Entity;
import com.lordmau5.harvest.shared.objects.IPickupable;
import com.lordmau5.harvest.shared.player.Player;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:32
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        if(msg instanceof PacketPlayerJoin) {
            Client.instance.playerJoin(((PacketPlayerJoin) msg).username);
            return;
        }
        else if(msg instanceof PacketPlayerLeave) {
            Client.instance.playerLeave(((PacketPlayerLeave) msg).username);
            return;
        }
        else if(msg instanceof PacketInitWorld) {
            PacketInitWorld wPacket = (PacketInitWorld) msg;

            World world = Client.getPlayerWorld();
            if(world == null) {
                world = new World();
                Client.setPlayerWorld(world);
            }
            world.setObjects(wPacket.objects);
            world.setFarmLand(wPacket.farmLand);
            return;
        }
        else if(msg instanceof PacketPlayerMovement) { // Let the player move *Kreygasm*
            PacketPlayerMovement move = (PacketPlayerMovement) msg;

            if(Client.instance == null)
                return;

            Player player = Client.instance.getPlayer(move.username);
            if(player == null || player == Client.getLocalPlayer())
                return;

            player.pFacing = move.direction;
            player.updatePos(move.x, move.y);

            if(!move.isStill)
                player.walk(player.pFacing, 1, move.isRunning);
            else
                player.playerAnim = player.holding != null ? new Animation(new Image[]{Player.playerAnims.get("carryStill").getImage(player.pFacing.ordinal())}, 1000) : new Animation(new Image[]{Player.playerAnims.get("stand").getImage(player.pFacing.ordinal())}, 1000);
            return;
        }
        else if(msg instanceof PacketPlayerPickupPlace) {
            PacketPlayerPickupPlace pkt = (PacketPlayerPickupPlace) msg;

            Player player = Client.instance.getPlayer(pkt.username);
            World world = player.getWorld();

            System.out.println(pkt.placeDown);

            if(pkt.placeDown) {
                if(world.isEntityInRectangle(new Rectangle(pkt.tile.getX() * 16 + 4, pkt.tile.getY() * 16 + 4, 7, 7)))
                    return;

                world.addEntity(pkt.entityName, pkt.tile.getX(), pkt.tile.getY());
                player.holding = null;
                return;
            }
            System.out.println(player.getUsername());

            Entity ent = world.getObjectAtPosition(pkt.tile);
            if(ent == null || !(ent instanceof IPickupable))
                return;

            world.removeObject(pkt.tile);
            player.holding = ent;
            return;
        }
        msg.process(NetworkHandler.connection);
    }
}