package com.lordmau5.harvest.shared.net.serialization;

import com.lordmau5.harvest.shared.serialization.ISerializer;
import com.lordmau5.harvest.shared.serialization.ISerializerFactory;
import com.lordmau5.harvest.shared.serialization.serializer.IntegerSerializer;

import java.util.HashMap;

public class PacketSerializerFactory implements ISerializerFactory {
    private final HashMap<Class, ISerializer> serializerHashMap;

    public PacketSerializerFactory() {
        serializerHashMap = new HashMap<>();
        registerSerializers();
    }

    private void registerSerializers() {
        serializerHashMap.clear();
        register(new IntegerSerializer());
    }

    private void register(ISerializer serializerClass) {
        serializerHashMap.put(serializerClass.getSerializeClass(), serializerClass);
    }

    @Override
    public ISerializer getSerializer(Class clazz) {
        return serializerHashMap.getOrDefault(clazz, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ISerializer<T> getTypedSerializer(Class clazz) {
        return getSerializer(clazz);
    }
}
