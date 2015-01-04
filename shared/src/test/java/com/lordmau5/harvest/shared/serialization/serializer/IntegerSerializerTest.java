package com.lordmau5.harvest.shared.serialization.serializer;

import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class IntegerSerializerTest {

    private final byte[] valueArray = {
            0x4E, 0x6F, 0x74, 0x20
    };
    private final int value = 544501582;

    private IntegerSerializer testee;

    @Before
    public void setUp() throws Exception {
        testee = new IntegerSerializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        // act
        int result = testee.deserialize(Unpooled.copiedBuffer(valueArray));

        // assert
        Assert.assertEquals(value, result);
    }

    @Test
    public void testSerialize() throws Exception {
        // arrange
        ByteArrayOutputStream mem = new ByteArrayOutputStream();

        // act
        testee.serialize(mem, value);

        // assert
        Assert.assertArrayEquals(valueArray, mem.toByteArray());
    }

    @Test
    public void testGetSerializeClass() throws Exception {
        // assert
        Assert.assertEquals(testee.getSerializeClass(), int.class);
    }
}