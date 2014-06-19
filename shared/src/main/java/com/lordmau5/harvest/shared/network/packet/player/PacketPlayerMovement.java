package com.lordmau5.harvest.shared.network.packet.player;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.PacketUtils;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.player.Player;
import com.lordmau5.harvest.shared.player.PlayerFacing;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 17:40
 */
public class PacketPlayerMovement extends Packet {
    public String username = "NULL";

    public PlayerFacing direction;
    public boolean isRunning;
    public float x;
    public float y;
    public boolean isStill;

    public PacketPlayerMovement() {

    }

    public PacketPlayerMovement(Player player, boolean isRunning, boolean isStill) {
        this.username = player.getUsername();

        this.direction = player.pFacing;
        this.isRunning = isRunning;
        this.x = player.pX;
        this.y = player.pY;
        this.isStill = isStill;
    }

    @Override
    public void encode(ByteBuf buffer) {
        PacketUtils.writeString(this.username, buffer);

        buffer.writeInt(this.direction.ordinal());
        buffer.writeBoolean(this.isRunning);
        buffer.writeFloat(this.x);
        buffer.writeFloat(this.y);
    }

    @Override
    public void decode(ByteBuf buffer) {
        this.username = PacketUtils.readString(buffer);

        this.direction = PlayerFacing.values()[buffer.readInt()];
        this.isRunning = buffer.readBoolean();
        this.x = buffer.readFloat();
        this.y = buffer.readFloat();
        this.isStill = buffer.readBoolean();
    }

    @Override
    public void process(ConnectionContext ctx) {

    }
}