package com.taotao.cloud.common.serializer.impl;

import com.taotao.cloud.common.serializer.Serializer;
import com.taotao.cloud.common.serializer.SerializerConstants;
import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class HexSerializer implements Serializer {
    @Override
    public String name() {
        return SerializerConstants.HEX;
    }

    @Override
    public byte[] serialize(Object o){
        if (o == null) {
            return new byte[0];
        }
        //只能转换字符串
        if(o instanceof String source){
			char[] chars = source.toCharArray();
            try {
                return Hex.decodeHex(chars);
            } catch (DecoderException e) {
                return new byte[0];
            }
        }
	    LogUtil.error("hex 只支持字符串序列化 ");
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes,ClassLoader classLoader) {
        return new String(Hex.encodeHex(bytes));
    }
}
