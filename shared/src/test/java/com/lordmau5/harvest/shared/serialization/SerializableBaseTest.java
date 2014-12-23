package com.lordmau5.harvest.shared.serialization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link com.lordmau5.harvest.shared.serialization.SerializableBase}
 */
public class SerializableBaseTest {
    private SerializableBase testee;

    @Before
    public void setUp() throws Exception {
        testee = new TestSerializable();
    }

    @Test
    public void testGetSerializableFieldsReturnsFields() throws Exception {
        // arrange
        final String[] expectedNames = {"Integer", "String"};
        final int[] pos = {0};

        // act & assert
        testee.getSerializableFields().forEachRemaining(
                x -> Assert.assertEquals(x.getName(), expectedNames[pos[0]++]));
    }

    @Test
    public void testGetClassidReturnsClassId() throws Exception {
        // arrange
        final int expectedClassId = 0xDEADBEEF;

        // act
        int result = testee.getClassId();

        // assert
        Assert.assertEquals(expectedClassId, result);
    }
}