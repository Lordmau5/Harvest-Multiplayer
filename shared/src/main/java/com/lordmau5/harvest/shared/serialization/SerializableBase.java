package com.lordmau5.harvest.shared.serialization;

import com.lordmau5.harvest.shared.serialization.annotations.ClassId;

import java.lang.reflect.Field;
import java.util.Iterator;

public class SerializableBase {
    private static final SerializableFieldIterator iterator = new SerializableFieldIterator();

    public Iterator<Field> getSerializableFields() {
        return iterator.init(getClass());
    }

    public int getClassId() {
        return getClass().getAnnotation(ClassId.class).id();
    }
}

