package com.lordmau5.harvest.shared.net.packet;

public interface IPacketFactory {
    public PacketBase createPacket(int classId);

    void register(Class<? extends PacketBase> packetClass);
}
