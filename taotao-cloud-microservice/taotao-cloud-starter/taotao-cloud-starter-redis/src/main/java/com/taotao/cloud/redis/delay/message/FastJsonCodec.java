package com.taotao.cloud.redis.delay.message;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.taotao.cloud.common.utils.JsonUtil;
import java.util.Map;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * FastJsonCodec 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class FastJsonCodec extends StringCodec {

    public static final FastJsonCodec INSTANCE = new FastJsonCodec();

    private  Encoder encoder;

    private  Decoder<Object> decoder;

    public FastJsonCodec() {
        this(StandardCharsets.UTF_8);
    }

    public FastJsonCodec(Charset charset) {
        super(charset);

        this.encoder = object -> {
            String jsonStr = JsonUtil.toJSONString(object);
            return super.getValueEncoder().encode(jsonStr);
        };

        this.decoder = (buf, state) -> {
            byte[] result = new byte[buf.readableBytes()];
            buf.readBytes(result);
	        String message = new String(result, StandardCharsets.UTF_8);
	        JsonNode jsonNode = JsonUtil.parse(message);

	        assert jsonNode != null;
	        String payload = jsonNode.get("payload").toString();
	        Map<String, Object> headers = JsonUtil.readMap(jsonNode.get("headers").toString());

	        //JsonNode payload = JsonUtil.parse(new String(result, StandardCharsets.UTF_8)).get("payload");
	        //String payloadStr = payload.traverse(JsonUtil.MAPPER).readValueAs(String.class);
	        //
	        //JsonNode headers = JsonUtil.parse(new String(result, StandardCharsets.UTF_8)).get("headers");
	        //Map<String, Object> headersMap = headers.traverse(JsonUtil.MAPPER).readValueAs(Map.class);

	        //JSONObject jsonObject = (JSONObject) JSON.parse(result);
            //Object payload = jsonObject.get("payload");
            //String payloadStr;
            //if (payload instanceof JSON) {
            //    payloadStr = ((JSON) payload).toJSONString();
            //} else {
            //    payloadStr = JSON.toJSONString(payload);
            //}
            //JSONObject headers = jsonObject.getJSONObject("headers");

            return new RedissonMessage(payload, headers);
        };
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
