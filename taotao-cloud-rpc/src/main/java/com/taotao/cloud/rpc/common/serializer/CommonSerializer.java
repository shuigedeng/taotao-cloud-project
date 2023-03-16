package com.taotao.cloud.rpc.common.serializer;


/**
 * 公共的序列化接口
 */
public interface CommonSerializer {

	Integer KRYO_SERIALIZER = 0;
	Integer JSON_SERIALIZER = 1;
	Integer HESSIAN_SERIALIZER = 2;
	Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

	byte[] serialize(Object obj);

	Object deserialize(byte[] bytes, Class<?> clazz);

	/**
	 * 网络序列化传输 最大化减少字节数，并且可以自动识别客户端采用的序列化方式并加以处理
	 *
	 * @return
	 */
	int getCode();

	/**
	 * 规定协议代码获取对应序列化方式
	 *
	 * @param code
	 * @return
	 */
	static CommonSerializer getByCode(int code) {
		switch (code) {
			case 0:
				return new KryoSerializer();
			case 1:
				return new JsonSerializer();
			case 2:
				return new HessianSerializer();
			default:
				return null;
		}
	}

}
