package com.lordmau5.harvest.shared.net;

import com.lordmau5.harvest.shared.net.serialization.annotations.ClassId;
import com.lordmau5.harvest.shared.net.serialization.annotations.SerializableField;

/**
 * Class used in various tests
 */
@ClassId(id = 0xDEADBEEF)
public class TestSerializable extends SerializableBase {
    @SerializableField
    public int Integer;

    @SerializableField
    public String String;
}