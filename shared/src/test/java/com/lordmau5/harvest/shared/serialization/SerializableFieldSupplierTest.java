package com.lordmau5.harvest.shared.serialization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Tests for {@link com.lordmau5.harvest.shared.serialization.SerializableFieldIterator}
 */
public class SerializableFieldSupplierTest {

    private SerializableFieldIterator testee;

    @Before
    public void setUp() throws Exception {
        testee = new SerializableFieldIterator();
        testee.init(TestSerializable.class);
    }

    @Test
    public void testGetReturnsFields() throws Exception {
        // act
        Field firstField = testee.next();
        Field secondField = testee.next();

        // assert
        Assert.assertSame(firstField.getName(), "Integer");
        Assert.assertSame(secondField.getName(), "String");
        Assert.assertFalse(testee.hasNext());
    }
}