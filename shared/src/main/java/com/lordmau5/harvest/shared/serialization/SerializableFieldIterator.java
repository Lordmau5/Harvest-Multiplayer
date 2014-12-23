package com.lordmau5.harvest.shared.serialization;

import java.lang.reflect.Field;
import java.util.Iterator;

public class SerializableFieldIterator implements Iterator<Field> {
    private Field[] fields;
    private int position;

    public SerializableFieldIterator init(Class<? extends SerializableBase> clazz) {
        fields = clazz.getFields();
        position = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return fields.length < position;
    }

    @Override
    public Field next() {
        return fields[position++];
    }
}
