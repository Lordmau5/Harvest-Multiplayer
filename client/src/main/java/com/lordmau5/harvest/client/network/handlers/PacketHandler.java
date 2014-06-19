package com.lordmau5.harvest.client.network.handlers;

import com.lordmau5.harvest.client.Client;
import com.lordmau5.harvest.client.network.NetworkHandler;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.network.packet.player.PacketPlayerMovement;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerLeave;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketSendPlayers;
import com.lordmau5.harvest.shared.player.Player;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

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
        else if(msg instanceof PacketSendPlayers) {
            Client.instance.updatePlayersFromServer(((PacketSendPlayers) msg).usernames);
        }
        else if(msg instanceof PacketPlayerMovement) { // Let the player move *Kreygasm*
            PacketPlayerMovement move = (PacketPlayerMovement) msg;

            if(Client.instance == null)
                return;

            Player player = Client.instance.getPlayer(move.username);
            if(player == null || player == Client.instance.getLocalPlayer())
                return;

            player.pFacing = move.direction;
            player.updatePos(move.x, move.y);

            if(!move.isStill)
                player.walk(player.pFacing, 1, move.isRunning);
            else
                player.playerAnim = player.holding != null ? new Animation(new Image[]{Player.playerAnims.get("carryStill").getImage(player.pFacing.ordinal())}, 1000) : new Animation(new Image[]{Player.playerAnims.get("stand").getImage(player.pFacing.ordinal())}, 1000);
            return;
        }
        msg.process(NetworkHandler.connection);
    }
}