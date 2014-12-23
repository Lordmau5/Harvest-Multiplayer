package com.lordmau5.harvest.shared.net.serialization;

import com.lordmau5.harvest.shared.net.packet.IPacketFactory;
import com.lordmau5.harvest.shared.net.packet.PacketBase;
import com.lordmau5.harvest.shared.net.packet.PacketFactory;
import com.lordmau5.harvest.shared.net.packet.TestPacket;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PacketSerializerTest {
    private PacketSerializer testee;

    @Before
    public void setUp() throws Exception {
        IPacketFactory factory = new PacketFactory();
        factory.register(TestPacket.class);
        testee = new PacketSerializer(factory);
    }

    @Test
    public void testDeserializePacket() throws Exception {
        // arrange
        byte[] arr = {(byte) 0xEF, (byte) 0xBE, (byte) 0xAD, (byte) 0xDE,
                0x01, 0x00, 0x00, 0x00,
                0x02, 0x00, 0x00, 0x00};

        // act
        PacketBase result = testee.deserialize(Unpooled.copiedBuffer(arr));

        // assert
        Assert.assertEquals(result.getClass(), TestPacket.class);

        TestPacket packet = (TestPacket) result;
        Assert.assertEquals(1, packet.Integer1);
        Assert.assertEquals(2, packet.Integer2);
    }

    @Test
    public void testSerializePacket() throws Exception {
        // arrange

        // act

        // assert
    }

    @Test
    public void testGetSerializeClassReturnsPacketBaseClass() throws Exception {
        Assert.assertEquals(testee.getSerializeClass(), PacketBase.class);
    }
}