package com.lordmau5.harvest.shared.serialization.serializer;

import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class StringSerializerTest {

    private final byte[] valueArray = {
            0x0C,
            0x4E, 0x6F, 0x74, 0x20,
            0x61, 0x6E, 0x20,
            0x41, 0x6E, 0x67, 0x65, 0x6c
    };
    private final String value = "Not an Angel";

    private StringSerializer testee;

    @Before
    public void setUp() throws Exception {
        testee = new StringSerializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        // act
        String result = testee.deserialize(Unpooled.copiedBuffer(valueArray));

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
        Assert.assertEquals(testee.getSerializeClass(), String.class);
    }
}