package com.taotao.cloud.redis.delay.message;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class FastJsonCodec extends StringCodec {

    public static final FastJsonCodec INSTANCE = new FastJsonCodec();

    private  Encoder encoder;

    private  Decoder<Object> decoder;

    public FastJsonCodec() {
        this(StandardCharsets.UTF_8);
    }

    public FastJsonCodec(Charset charset) {
        super(charset);
        //this.encoder = object -> {
        //    String jsonStr = JSON.toJSONString(object);
        //    return super.getValueEncoder().encode(jsonStr);
        //};
		//
        //this.decoder = (buf, state) -> {
        //    byte[] result = new byte[buf.readableBytes()];
        //    buf.readBytes(result);
        //    JSONObject jsonObject = (JSONObject) JSON.parse(result);
        //    Object payload = jsonObject.get("payload");
        //    String payloadStr;
        //    if (payload instanceof JSON) {
        //        payloadStr = ((JSON) payload).toJSONString();
        //    } else {
        //        payloadStr = JSON.toJSONString(payload);
        //    }
        //    JSONObject headers = jsonObject.getJSONObject("headers");
        //    return new RedissonMessage(payload == null ? new byte[]{} : payloadStr.getBytes(charset), headers);
        //};
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return this.decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return this.encoder;
    }

}
