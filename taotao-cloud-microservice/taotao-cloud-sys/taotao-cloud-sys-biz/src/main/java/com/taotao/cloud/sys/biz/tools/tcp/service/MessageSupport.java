package com.taotao.cloud.sys.biz.tools.tcp.service;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageSupport {

    public byte[] calcData(String hex, String ascii) throws DecoderException {
        byte [] data = null;
        if (StringUtils.isNotBlank(hex)){
            data = Hex.decodeHex(hex);
        }else if (StringUtils.isNotBlank(ascii)){
            data = ascii.getBytes();
        }else{
            data = "Hello".getBytes();
        }
        return data;
    }
}
