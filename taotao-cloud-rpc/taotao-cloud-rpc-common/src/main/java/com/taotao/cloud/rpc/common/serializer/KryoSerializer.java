package com.taotao.cloud.rpc.common.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.taotao.cloud.rpc.common.enums.SerializerCode;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class KryoSerializer implements CommonSerializer {

	private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
		Kryo kryo = new Kryo();
		kryo.register(RpcResponse.class);
		kryo.register(RpcRequest.class);
		kryo.setReferences(true);
		kryo.setRegistrationRequired(false);
		return kryo;
	});

	@Override
	public byte[] serialize(Object obj) {
		byte[] data = null;
		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
			//OutputChunked output = new OutputChunked(byteArrayOutputStream, 1000000);
			Output output = new Output(os, 1000000)) {

			Kryo kryo = kryoThreadLocal.get();
			kryo.writeObject(output, obj);
			kryoThreadLocal.remove();
			output.flush();
			output.getOutputStream().flush();
			data = os.toByteArray();
		} catch (IOException e) {
			log.error("Error occurred while serializing: ", e);
		}
		return data;
	}

	@Override
	public Object deserialize(byte[] data, Class<?> clazz) {
		if (data == null) {
			return null;
		}
		Object obj = null;
		try (ByteArrayInputStream is = new ByteArrayInputStream(data);
			//InputChunked input = new InputChunked(byteArrayInputStream, 1000000);
			Input input = new Input(is, data.length);
		) {
			Kryo kryo = kryoThreadLocal.get();
			obj = kryo.readObject(input, clazz);
			kryoThreadLocal.remove();
		} catch (IOException e) {
			log.error("Error occurred while deserializing: {}", e);
		}
		return obj;
	}

	@Override
	public int getCode() {
		return SerializerCode.valueOf("KRYO").getCode();
	}
}
