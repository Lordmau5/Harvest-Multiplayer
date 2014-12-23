package com.lordmau5.harvest.shared.net.serialization;

import com.lordmau5.harvest.shared.net.packet.PacketBase;

public class PacketSerializer extends SerializerBase<PacketBase> {
    @Override
    public PacketBase deserialize(byte[] input) {
        return null;
    }

    @Override
    public byte[] serialize(PacketBase input) {
        return new byte[0];
    }
}
