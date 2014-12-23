package com.lordmau5.harvest.shared.serialization;

import com.lordmau5.harvest.shared.serialization.annotations.AnnotationHelper;
import com.lordmau5.harvest.shared.serialization.annotations.SerializableField;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class SerializableFieldIterator implements Iterator<Field> {
    private Queue<Field> fields;

    public SerializableFieldIterator init(Class<? extends SerializableBase> clazz) {
        fields = new ArrayDeque<>();
        for (Field f : clazz.getFields()) {
            if (AnnotationHelper.hasAnnotation(f, SerializableField.class)) {
                fields.add(f);
            }
        }
        return this;
    }

    @Override
    public boolean hasNext() {
        return !fields.isEmpty();
    }

    @Override
    public Field next() {
        return fields.poll();
    }
}
