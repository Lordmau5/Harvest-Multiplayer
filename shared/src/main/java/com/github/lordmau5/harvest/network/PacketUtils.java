package com.github.lordmau5.harvest.network;

import io.netty.buffer.ByteBuf;

/**
 * No description given
 *
 * @author jk-5
 */
public class PacketUtils {

    public static int readVarInt(ByteBuf input){
        int out = 0;
        int bytes = 0;
        byte in;
        while(true){
            in = input.readByte();
            out |= (in & 127) << (bytes++ * 7);
            if(bytes > 5){
                throw new RuntimeException("VarInt too big");
            }
            if((in & 128) != 128){
                break;
            }
        }
        return out;
    }

    public static void writeVarInt(int value, ByteBuf output){
        int part;
        while(true){
            part = value & 127;
            value >>>= 7;
            if(value != 0){
                part |= 128;
            }
            output.writeByte(part);
            if(value == 0){
                break;
            }
        }
    }

    public static int varIntSize(int varint){
        if((varint & 0xFFFFFF80) == 0){
            return 1;
        }
        if((varint & 0xFFFFC000) == 0){
            return 2;
        }
        if((varint & 0xFFE00000) == 0){
            return 3;
        }
        if((varint & 0xF0000000) == 0){
            return 4;
        }
        return 5;
    }
}