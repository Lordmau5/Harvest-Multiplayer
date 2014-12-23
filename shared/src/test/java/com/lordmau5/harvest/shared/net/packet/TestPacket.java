package com.lordmau5.harvest.shared.net.packet;

import com.lordmau5.harvest.shared.serialization.annotations.ClassId;
import com.lordmau5.harvest.shared.serialization.annotations.SerializableField;

@ClassId(id = 0xDEADBEEF)
public class TestPacket extends PacketBase {
    @SerializableField
    public int Integer1;

    @SerializableField
    public int Integer2;
}
