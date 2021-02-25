package com.taotao.cloud.netty.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufTest0 {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; ++i) {
            buffer.writeByte(i);
        }

//        for (int i = 0; i < buffer.capacity(); ++i) {
//            System.out.println(buffer.getByte(i));
//        }


        for (int i = 0; i < buffer.capacity(); ++i) {
            System.out.println(buffer.readByte());
        }
    }
}
