package com.lordmau5.harvest.shared.network.codec;

import com.lordmau5.harvest.shared.network.packet.handshake.PacketCloseConnection;
import com.lordmau5.harvest.shared.network.packet.player.action.PacketPlayerPickupPlace;
import com.lordmau5.harvest.shared.network.packet.player.movement.PacketPlayerMovement;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerLeave;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketRequestPlayers;
import com.lordmau5.harvest.shared.network.packet.world.PacketInitWorld;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:46
 */
public class PlayCodecV1 extends PacketCodec {
    public PlayCodecV1() {
        //TODO: add gameplay packets
        this.registerPacket(1, PacketCloseConnection.class);

        this.registerPacket(2, PacketPlayerJoin.class);
        this.registerPacket(3, PacketPlayerLeave.class);
        this.registerPacket(4, PacketRequestPlayers.class);
        this.registerPacket(5, PacketInitWorld.class);

        this.registerPacket(6, PacketPlayerMovement.class);
        this.registerPacket(7, PacketPlayerPickupPlace.class);
    }
}