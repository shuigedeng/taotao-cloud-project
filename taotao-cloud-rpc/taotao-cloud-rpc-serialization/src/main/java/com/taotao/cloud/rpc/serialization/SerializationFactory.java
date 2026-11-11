package com.taotao.cloud.rpc.serialization;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.RpcException;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化器工厂
 * <p>
 * 按 contentTypeId 获取对应的序列化器实例，单例缓存。
 */
public class SerializationFactory {

    private static final Logger log = LogManager.getLogger(SerializationFactory.class);

    private static final Map<Byte, Serialization> SERIALIZERS = new ConcurrentHashMap<>();

    /**
     * 装载来源是 {@code META-INF/ttc-rpc/com.taotao.cloud.rpc.serialize.Serialization} 这张 SPI 表，
     * 而不是在这里自己 {@code new} 五个。
     * <p>
     * 曾经静态块直接 new，于是同一个 "kryo" 在 {@link ExtensionLoader} 和这里各有一个人家
     * （两张注册表手递不同实例，谁也不知道另一个的存在）。走 SPI 之后两边拿到的是同一个对象，
     * 而且往元文件里加一个新实现就能被这里看见。
     */
    private static final String[] SPI_NAMES = {"java", "json", "hessian2", "kryo", "protobuf"};

    static {
        ExtensionLoader<Serialization> loader = ExtensionLoader.getExtensionLoader(Serialization.class);
        for (String name : SPI_NAMES) {
            try {
                register(loader.getExtension(name));
            } catch (RuntimeException e) {
                log.warn("Serialization '{}' is listed in the SPI file but could not be loaded: {}",
                        name, e.toString());
            }
        }
    }

    /**
     * 注册一个序列化器
     */
    public static void register(Serialization serialization) {
        if (serialization == null) return;
        SERIALIZERS.put(serialization.getContentTypeId(), serialization);
        log.info("Registered serialization: id=0x{}, class={}",
                String.format("%02X", serialization.getContentTypeId()),
                serialization.getClass().getSimpleName());
    }

    /**
     * 按 contentTypeId 获取序列化器
     *
     * @param id 协议头中的序列化器 ID
     * @return 序列化器实例
     * @throws RpcException id 没有对应的序列化器 —— <b>不回落</b>到任何一种默认实现：
     *                      对端配错时用错误的解码器去解字节，只会把"一连就报错"变成"随机乱码/莫名异常"
     */
    public static Serialization get(byte id) {
        Serialization ser = SERIALIZERS.get(id);
        if (ser == null) {
            throw RpcException.serialization(
                    "No serialization registered for contentTypeId 0x"
                            + String.format("%02X", id)
                            + " (registered: " + registeredIds() + ")");
        }
        return ser;
    }

    private static String registeredIds() {
        StringBuilder sb = new StringBuilder();
        for (Byte b : SERIALIZERS.keySet()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(String.format("0x%02X", b));
        }
        return sb.toString();
    }

    /**
     * 按名称获取（如 "hessian2" / "json"）
     *
     * @throws IllegalArgumentException  name 为 null
     * @throws RpcException              name 不是本框架认识的序列化器 —— 以前这里返回 null，
     *                                   调用方拿到的是第一次解引用时的 NPE，堆栈里看不到"名字写错了"这个真因
     */
    public static Serialization getByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("serialization name must not be null");
        }
        switch (name.toLowerCase()) {
            case "java":     return require(name, ProtocolConstants.SERIALIZE_JAVA);
            case "json":     return require(name, ProtocolConstants.SERIALIZE_JSON);
            case "hessian2": return require(name, ProtocolConstants.SERIALIZE_HESSIAN2);
            case "kryo":     return require(name, ProtocolConstants.SERIALIZE_KRYO);
            case "protobuf": return require(name, ProtocolConstants.SERIALIZE_PROTOBUF);
            default:
                throw RpcException.config("Unknown serialization name: '" + name
                        + "'; supported: java, json, hessian2, kryo, protobuf");
        }
    }

    /** 认得这个名字但对应实现没装载起来（例如 SPI 元文件被裁过）—— 也要炸，不能返回 null。 */
    private static Serialization require(String name, byte id) {
        Serialization ser = SERIALIZERS.get(id);
        if (ser == null) {
            throw RpcException.config("Serialization '" + name + "' (id 0x"
                    + String.format("%02X", id) + ") is not registered on this classpath");
        }
        return ser;
    }
}
