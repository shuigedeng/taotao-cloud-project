package com.taotao.cloud.mq.consistency.ttc.store.log;

import com.taotao.cloud.mq.consistency.ttc.store.MessageExtBrokerInner;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.CRC32;

/**
 * CommitLog 记录编解码（V2 定长格式）.
 * <p>
 * 定长区 88 字节，偏移从记录起点算（与工单 W1 §1 的表一一对应）：
 * <pre>
 *  0 totalSize(int)   4 magic(int)      8 crc(int)        12 queueId(int)
 * 16 sysFlag(int)    20 flag(int)      24 bornTimestamp(long)
 * 32 storeTimestamp(long)              40 queueOffset(long)
 * 48 commitLogOffset(long)             56 bodyCRC(int)
 * 60 reconsumeTimes(int)               64 preparedTransactionOffset(long)
 * 72 msgIdLen(short) 74 topicLen(short) 76 propsLen(short) 78 bodyLen(int)
 * 82 bornHostLen(short) 84 storeHostLen(short) 86 reserved(short, 恒 0)
 * </pre>
 * 变长区顺序：{@code msgId | topic | props | body | bornHost | storeHost}。
 * host 编码成 {@code "1.2.3.4:9876"} 形式的 UTF-8 字符串（null ⇒ 长度 0），IPv6 不需要特判。
 * <p>
 * {@code crc} 覆盖 {@code [12, totalSize)}，即 crc 字段之后的全部字节（含 commitLogOffset，
 * 因此编码必须在物理偏移已知之后进行）。
 * <p>
 * props 区是自描述的：{@code int count} 之后每条 {@code short nameLen + name + int valueLen + value}，
 * 解码可以完整还原 {@code Message.getProperties()}。{@code tags}/{@code keys} 这两个
 * {@code Message} 自有字段不在定长区里，按 RocketMQ 惯例折进 props（键名 {@code TAGS}/{@code KEYS}），
 * 解码时再抬回专用字段并从用户属性里移除。
 */
public final class MessageCodec {

    /** 定长区大小（字节）。 */
    public static final int FIXED_AREA_SIZE = 88;

    // ---- 定长区偏移 ----
    public static final int POS_TOTAL_SIZE = 0;
    public static final int POS_MAGIC = 4;
    public static final int POS_CRC = 8;
    public static final int POS_QUEUE_ID = 12;
    public static final int POS_SYS_FLAG = 16;
    public static final int POS_FLAG = 20;
    public static final int POS_BORN_TIMESTAMP = 24;
    public static final int POS_STORE_TIMESTAMP = 32;
    public static final int POS_QUEUE_OFFSET = 40;
    public static final int POS_COMMIT_LOG_OFFSET = 48;
    public static final int POS_BODY_CRC = 56;
    public static final int POS_RECONSUME_TIMES = 60;
    public static final int POS_PREPARED_TX_OFFSET = 64;
    public static final int POS_MSGID_LEN = 72;
    public static final int POS_TOPIC_LEN = 74;
    public static final int POS_PROPS_LEN = 76;
    public static final int POS_BODY_LEN = 78;
    public static final int POS_BORN_HOST_LEN = 82;
    public static final int POS_STORE_HOST_LEN = 84;

    /** props 区里承载 Message.tags 的键名（解码后抬回 Message.setTags）。 */
    public static final String PROP_TAGS = "TAGS";
    /** props 区里承载 Message.keys 的键名（解码后抬回 Message.setKeys）。 */
    public static final String PROP_KEYS = "KEYS";

    /** 短整型长度上限（msgId/topic/props/host 用 short 记长度）。 */
    private static final int SHORT_LEN_MAX = Short.MAX_VALUE;

    private MessageCodec() {
    }

    /**
     * 计算一条记录编码后的总长度，不需要真正编码（供写入前判断文件剩余空间）。
     *
     * @return 总字节数；某个变长字段超过 short 上限时返回 -1（消息非法）
     */
    public static int encodedSize(MessageExtBrokerInner msg) {
        int msgIdLen = utf8Len(msg.getMsgId());
        int topicLen = utf8Len(msg.getTopic());
        int propsLen = propsSize(collectProperties(msg));
        int bodyLen = msg.getBody() != null ? msg.getBody().length : 0;
        int bornHostLen = utf8Len(hostToString(msg.getBornHost()));
        int storeHostLen = utf8Len(hostToString(msg.getStoreHost()));
        if (msgIdLen > SHORT_LEN_MAX || topicLen > SHORT_LEN_MAX || propsLen > SHORT_LEN_MAX
                || bornHostLen > SHORT_LEN_MAX || storeHostLen > SHORT_LEN_MAX) {
            return -1;
        }
        return FIXED_AREA_SIZE + msgIdLen + topicLen + propsLen + bodyLen + bornHostLen + storeHostLen;
    }

    /**
     * 编码为 V2 记录。
     *
     * @param msg             待写消息
     * @param commitLogOffset 该记录在 CommitLog 中的物理起始偏移（必须在定稿前已知，crc 覆盖它）
     * @return 长度为 totalSize 的字节数组；字段超长时返回 null
     */
    public static byte[] encode(MessageExtBrokerInner msg, long commitLogOffset) {
        final int total = encodedSize(msg);
        if (total < 0) {
            return null;
        }

        final byte[] msgIdData = utf8(msg.getMsgId());
        final byte[] topicData = utf8(msg.getTopic());
        final byte[] propsData = serializeProperties(collectProperties(msg));
        final byte[] bodyData = msg.getBody() != null ? msg.getBody() : new byte[0];
        final byte[] bornHostData = utf8(hostToString(msg.getBornHost()));
        final byte[] storeHostData = utf8(hostToString(msg.getStoreHost()));

        ByteBuffer buffer = ByteBuffer.allocate(total);
        buffer.putInt(total);
        buffer.putInt(CommitLog.MESSAGE_MAGIC_CODE_V2);
        buffer.putInt(0); // crc 占位，最后回填
        buffer.putInt(msg.getQueueId());
        buffer.putInt(msg.getSysFlag());
        buffer.putInt(msg.getFlag());
        buffer.putLong(msg.getBornTimestamp());
        buffer.putLong(msg.getStoreTimestamp());
        buffer.putLong(msg.getQueueOffset());
        buffer.putLong(commitLogOffset);
        buffer.putInt(msg.getBodyCRC());
        buffer.putInt(msg.getReconsumeTimes());
        buffer.putLong(msg.getPreparedTransactionOffset());
        buffer.putShort((short) msgIdData.length);
        buffer.putShort((short) topicData.length);
        buffer.putShort((short) propsData.length);
        buffer.putInt(bodyData.length);
        buffer.putShort((short) bornHostData.length);
        buffer.putShort((short) storeHostData.length);
        buffer.putShort((short) 0); // 86..87 保留位，保证定长区恰好 88 字节
        buffer.put(msgIdData);
        buffer.put(topicData);
        buffer.put(propsData);
        buffer.put(bodyData);
        buffer.put(bornHostData);
        buffer.put(storeHostData);

        final int crc = crc32(buffer.array(), POS_CRC + 4, total - (POS_CRC + 4));
        buffer.putInt(POS_CRC, crc);
        return buffer.array();
    }

    /** {@link #verifyRecord} / {@link #decode} 表示"记录非法"的返回值。 */
    public static final int INVALID = -1;

    /**
     * 读取记录头并做结构校验（magic / 总长越界 / 各变长字段自洽 / 物理偏移一致），
     * 用于恢复扫描与"先探长度再取整条"的读路径。
     * <p>
     * crc 只在视图确实装下了整条记录时才校验（{@code buffer.limit() >= totalSize}）——
     * 恢复扫描先只看 88 字节定长头，取到整条之后由 {@link #decode} 负责 crc 与内容自洽。
     *
     * @param buffer      索引 0 == 记录起点的缓冲区视图，至少含定长区
     * @param fileSize    从记录起点到可读区末尾还剩多少字节（做越界校验）
     * @param expectStart 期望记录的物理起始偏移（校验定长区里的 commitLogOffset 与实际位置一致）；
     *                    传负数表示不校验
     * @return 记录总长度；magic 不符 / 长度越界 / 偏移不符 / crc 不符时返回 {@link #INVALID}
     */
    public static int verifyRecord(ByteBuffer buffer, final int fileSize, final long expectStart) {
        if (buffer == null || fileSize < FIXED_AREA_SIZE) {
            return INVALID;
        }
        final int magic = buffer.getInt(POS_MAGIC);
        if (CommitLog.MESSAGE_MAGIC_CODE_V2 != magic) {
            return INVALID;
        }
        final int totalSize = buffer.getInt(POS_TOTAL_SIZE);
        if (totalSize < FIXED_AREA_SIZE || totalSize > fileSize) {
            return INVALID;
        }
        final int bodyLen = buffer.getInt(POS_BODY_LEN);
        final int msgIdLen = buffer.getShort(POS_MSGID_LEN) & 0xFFFF;
        final int topicLen = buffer.getShort(POS_TOPIC_LEN) & 0xFFFF;
        final int propsLen = buffer.getShort(POS_PROPS_LEN) & 0xFFFF;
        final int bornHostLen = buffer.getShort(POS_BORN_HOST_LEN) & 0xFFFF;
        final int storeHostLen = buffer.getShort(POS_STORE_HOST_LEN) & 0xFFFF;
        final long varSum = (long) msgIdLen + topicLen + propsLen + bodyLen + bornHostLen + storeHostLen;
        if (bodyLen < 0 || varSum != (totalSize - FIXED_AREA_SIZE)) {
            return INVALID;
        }
        if (expectStart >= 0 && buffer.getLong(POS_COMMIT_LOG_OFFSET) != expectStart) {
            return INVALID;
        }
        if (buffer.limit() >= totalSize) {
            // 视图装得下整条记录才谈得上 crc
            final int storedCrc = buffer.getInt(POS_CRC);
            final int calcCrc = crc32(buffer, POS_CRC + 4, totalSize - (POS_CRC + 4));
            if (storedCrc != calcCrc) {
                return INVALID;
            }
        }
        return totalSize;
    }

    /**
     * 解码一条完整记录。
     *
     * @param buffer 索引 0 == 记录起点的缓冲区视图（由 MappedFile.selectMappedBuffer 提供）
     * @return 还原出的消息（含全部 13 个 MessageExt 字段 + topic/properties/body/hosts）；
     *         记录非法时返回 null
     */
    public static MessageExtBrokerInner decode(ByteBuffer buffer) {
        if (buffer == null || buffer.limit() < FIXED_AREA_SIZE) {
            return null;
        }
        final int magic = buffer.getInt(POS_MAGIC);
        if (CommitLog.MESSAGE_MAGIC_CODE_V2 != magic) {
            return null;
        }
        final int totalSize = buffer.getInt(POS_TOTAL_SIZE);
        if (totalSize < FIXED_AREA_SIZE || totalSize > buffer.limit()) {
            return null;
        }
        final int storedCrc = buffer.getInt(POS_CRC);
        if (storedCrc != crc32(buffer, POS_CRC + 4, totalSize - (POS_CRC + 4))) {
            return null;
        }

        final int msgIdLen = buffer.getShort(POS_MSGID_LEN) & 0xFFFF;
        final int topicLen = buffer.getShort(POS_TOPIC_LEN) & 0xFFFF;
        final int propsLen = buffer.getShort(POS_PROPS_LEN) & 0xFFFF;
        final int bodyLen = buffer.getInt(POS_BODY_LEN);
        final int bornHostLen = buffer.getShort(POS_BORN_HOST_LEN) & 0xFFFF;
        final int storeHostLen = buffer.getShort(POS_STORE_HOST_LEN) & 0xFFFF;
        if (bodyLen < 0
                || (long) msgIdLen + topicLen + propsLen + bodyLen + bornHostLen + storeLen(storeHostLen)
                != totalSize - FIXED_AREA_SIZE) {
            return null;
        }

        int pos = FIXED_AREA_SIZE;
        final String msgId = pos + msgIdLen > totalSize ? null : fromUtf8(buffer, pos, msgIdLen);
        pos += msgIdLen;
        final String topic = pos + topicLen > totalSize ? null : fromUtf8(buffer, pos, topicLen);
        pos += topicLen;
        final byte[] propsBytes = bytes(buffer, pos, propsLen);
        pos += propsLen;
        final byte[] body = bytes(buffer, pos, bodyLen);
        pos += bodyLen;
        final String bornHostStr = fromUtf8(buffer, pos, bornHostLen);
        pos += bornHostLen;
        final String storeHostStr = fromUtf8(buffer, pos, storeHostLen);

        MessageExtBrokerInner msg = new MessageExtBrokerInner();
        msg.setMsgId(emptyToNull(msgId));
        msg.setTopic(emptyToNull(topic));
        msg.setQueueId(buffer.getInt(POS_QUEUE_ID));
        msg.setSysFlag(buffer.getInt(POS_SYS_FLAG));
        msg.setFlag(buffer.getInt(POS_FLAG));
        msg.setBornTimestamp(buffer.getLong(POS_BORN_TIMESTAMP));
        msg.setStoreTimestamp(buffer.getLong(POS_STORE_TIMESTAMP));
        msg.setQueueOffset(buffer.getLong(POS_QUEUE_OFFSET));
        msg.setCommitLogOffset(buffer.getLong(POS_COMMIT_LOG_OFFSET));
        msg.setBodyCRC(buffer.getInt(POS_BODY_CRC));
        msg.setReconsumeTimes(buffer.getInt(POS_RECONSUME_TIMES));
        msg.setPreparedTransactionOffset(buffer.getLong(POS_PREPARED_TX_OFFSET));
        msg.setBody(body);
        msg.setStoreSize(totalSize);
        msg.setBornHost(parseHost(bornHostStr));
        msg.setStoreHost(parseHost(storeHostStr));

        Map<String, String> props = deserializeProperties(propsBytes);
        if (props != null) {
            String tags = props.remove(PROP_TAGS);
            String keys = props.remove(PROP_KEYS);
            msg.setTags(tags);
            msg.setKeys(keys);
            msg.setProperties(props);
            msg.setPropertiesString(renderPropertiesString(props, tags, keys));
        } else {
            msg.setProperties(new HashMap<String, String>());
            msg.setPropertiesString("");
        }
        return msg;
    }

    /** 从记录起点读 magic（调用方只需 4 字节可读时用它判断 BLANK / 坏数据）。 */
    public static int magicOf(ByteBuffer buffer) {
        if (buffer == null || buffer.limit() < 4) {
            return 0;
        }
        return buffer.getInt(POS_MAGIC);
    }

    // ==================== props 序列化 ====================

    /**
     * 收集要落盘的属性：Message.properties，外加 tags/keys 折进来的两个键。
     * 若调用方只给了 propertiesString（Broker 侧的 "k=v\n" 渲染）而没给 map，这里会把字符串解析回来，
     * 保证不丢属性。
     */
    private static Map<String, String> collectProperties(MessageExtBrokerInner msg) {
        Map<String, String> src = msg.getProperties();
        Map<String, String> props = new TreeMap<String, String>();
        if (src != null && !src.isEmpty()) {
            for (Map.Entry<String, String> e : src.entrySet()) {
                if (e.getKey() != null) {
                    props.put(e.getKey(), e.getValue() == null ? "" : e.getValue());
                }
            }
        } else if (msg.getPropertiesString() != null && !msg.getPropertiesString().isEmpty()) {
            props.putAll(parsePropertiesString(msg.getPropertiesString()));
        }
        if (msg.getTags() != null && !msg.getTags().isEmpty()) {
            props.put(PROP_TAGS, msg.getTags());
        }
        if (msg.getKeys() != null && !msg.getKeys().isEmpty()) {
            props.put(PROP_KEYS, msg.getKeys());
        }
        return props;
    }

    private static int propsSize(Map<String, String> props) {
        int size = 4;
        for (Map.Entry<String, String> e : props.entrySet()) {
            size += 2 + utf8Len(e.getKey()) + 4 + utf8Len(e.getValue());
        }
        return size;
    }

    private static byte[] serializeProperties(Map<String, String> props) {
        ByteBuffer buf = ByteBuffer.allocate(propsSize(props));
        buf.putInt(props.size());
        for (Map.Entry<String, String> e : props.entrySet()) {
            byte[] k = utf8(e.getKey());
            byte[] v = utf8(e.getValue());
            buf.putShort((short) k.length);
            buf.put(k);
            buf.putInt(v.length);
            buf.put(v);
        }
        byte[] out = new byte[buf.position()];
        System.arraycopy(buf.array(), 0, out, 0, out.length);
        return out;
    }

    /** @return 解析失败（长度不自洽）返回 null，调用方按"无属性"处理 */
    private static Map<String, String> deserializeProperties(byte[] data) {
        if (data == null || data.length < 4) {
            return new HashMap<String, String>();
        }
        ByteBuffer buf = ByteBuffer.wrap(data);
        int count = buf.getInt();
        if (count < 0 || count > 100000) {
            return null;
        }
        Map<String, String> props = new HashMap<String, String>();
        try {
            for (int i = 0; i < count; i++) {
                int kLen = buf.getShort() & 0xFFFF;
                byte[] k = new byte[kLen];
                buf.get(k);
                int vLen = buf.getInt();
                if (vLen < 0 || vLen > buf.remaining()) {
                    return null;
                }
                byte[] v = new byte[vLen];
                buf.get(v);
                props.put(new String(k, StandardCharsets.UTF_8), new String(v, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            return null;
        }
        return props;
    }

    private static Map<String, String> parsePropertiesString(String text) {
        Map<String, String> props = new HashMap<String, String>();
        for (String line : text.split("\n")) {
            if (line.isEmpty()) {
                continue;
            }
            int idx = line.indexOf('=');
            if (idx > 0) {
                props.put(line.substring(0, idx), line.substring(idx + 1));
            }
        }
        return props;
    }

    private static String renderPropertiesString(Map<String, String> props, String tags, String keys) {
        StringBuilder sb = new StringBuilder();
        if (props != null) {
            for (Map.Entry<String, String> e : new TreeMap<String, String>(props).entrySet()) {
                sb.append(e.getKey()).append('=').append(e.getValue()).append('\n');
            }
        }
        if (tags != null) {
            sb.append(PROP_TAGS).append('=').append(tags).append('\n');
        }
        if (keys != null) {
            sb.append(PROP_KEYS).append('=').append(keys).append('\n');
        }
        return sb.toString();
    }

    // ==================== host ====================

    private static String hostToString(java.net.InetSocketAddress host) {
        if (host == null) {
            return null;
        }
        return host.getHostString() + ":" + host.getPort();
    }

    static java.net.InetSocketAddress parseHost(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        int idx = text.lastIndexOf(':');
        if (idx <= 0 || idx == text.length() - 1) {
            return null;
        }
        String host = text.substring(0, idx);
        try {
            int port = Integer.parseInt(text.substring(idx + 1));
            return java.net.InetSocketAddress.createUnresolved(host, port);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ==================== 小工具 ====================

    private static int storeLen(int storeHostLen) {
        return storeHostLen;
    }

    private static byte[] utf8(String s) {
        return s == null ? new byte[0] : s.getBytes(StandardCharsets.UTF_8);
    }

    private static int utf8Len(String s) {
        return s == null ? 0 : s.getBytes(StandardCharsets.UTF_8).length;
    }

    private static String fromUtf8(ByteBuffer buffer, final int pos, final int len) {
        return new String(bytes(buffer, pos, len), StandardCharsets.UTF_8);
    }

    private static byte[] bytes(ByteBuffer buffer, final int pos, final int len) {
        byte[] out = new byte[len];
        if (len > 0) {
            ByteBuffer dup = buffer.duplicate();
            dup.position(pos);
            dup.limit(pos + len);
            dup.get(out);
        }
        return out;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    private static int crc32(byte[] data, final int offset, final int length) {
        CRC32 crc = new CRC32();
        crc.update(data, offset, length);
        return (int) crc.getValue();
    }

    /** 对缓冲区绝对区间 [pos, pos+length) 求 crc32，不改变缓冲区 position。 */
    static int crc32(ByteBuffer buffer, final int pos, final int length) {
        return crc32(bytes(buffer, pos, length), 0, length);
    }
}
