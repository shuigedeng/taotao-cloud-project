package com.taotao.cloud.mq.consistency.ttc.store.log;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.store.AppendMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.MessageExtBrokerInner;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageStatus;
import com.taotao.cloud.mq.consistency.ttc.store.log.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * CommitLog 实现
 * 消息存储主文件，所有消息顺序写入CommitLog
 * <p>
 * 记录格式 V2（{@link #MESSAGE_MAGIC_CODE_V2}），定长区 88 字节 + 变长区，
 * 逐字段偏移见 {@link MessageCodec}。旧格式 {@code 0xAABBCCDD} 不再兼容读取：
 * 这是未发布的 1.2.0→1.3.0 内部格式，扫描时读到 magic 不是 V2 就视为"扫描到此为止"。
 * <p>
 * 存储结构（一条记录）：
 * <pre>
 * ┌────────┬────────┬────────┬─────────┬────────┬──────────┬──────────────┐
 * │totalSize│ magic  │ crc32  │ queueId │sysFlag │  flag    │bornTimestamp │
 * ├────────┼────────┼────────┼─────────┼────────┼──────────┼──────────────┤
 * │storeTs │queueOff│commitLo│bodyCRC  │reconsume│preparedTx│ msgId/topic/ │
 * │(8B)    │set(8B) │gOff(8B)│(4B)     │Times(4B)│TxOff(8B)│ props/body/…  │
 * └────────┴────────┴────────┴─────────┴────────┴──────────┴──────────────┘
 * </pre>
 * <p>
 * 位点分配：{@code queueOffset} 在<b>追加之前</b>由队列索引分配，物理 {@code commitLogOffset} 在编码之前
 * 定位，两者与"落盘 + 建索引"在 {@link #putMessageLock} 同一把锁里完成（粗粒度锁，正确性优先）。
 */
public class CommitLog {

    // 魔数（V1，已废弃，仅保留常量给历史断言用；不再有任何写路径使用它）
    public static final int MESSAGE_MAGIC_CODE = 0xAABBCCDD;
    // 消息魔数（用于判断消息合法性）
    public static final int MESSAGE_MAGIC_CODE_V2 = 0xCDDDABBA;
    // 空白魔数（文件剩余空间放不下下一条记录时用于封尾）
    public static final int BLANK_MAGIC_CODE = 0xBBCCDDEE;
    /**
     * rootDir 下的 checkpoint 文件：8 字节大端 long，"已确认落盘的最大连续物理偏移"。
     */
    public static final String CHECKPOINT_FILE_NAME = "checkpoint";
    /**
     * rootDir 下的 abort 文件：start() 创建、干净 shutdown() 删除；启动时仍在 ⇒ 上次非干净退出。
     */
    public static final String ABORT_FILE_NAME = "abort";
    private static final Logger log = LoggerFactory.getLogger(CommitLog.class);
    // 默认消息体大小（用于分配缓冲区）
    private static final int MAX_MESSAGE_SIZE = 1024 * 1024 * 4; // 4MB
    // 恢复扫描时读的定长区视图大小
    private static final int RECOVER_HEADER_SIZE = MessageCodec.FIXED_AREA_SIZE;
    // 消息存储配置
    private final MessageStoreConfig messageStoreConfig;
    // MappedFile队列
    private final MappedFileQueue mappedFileQueue;
    // 刷盘服务
    private final FlushCommitLogService flushCommitLogService;
    // 进程内 Queue 位点索引 — 只存 {queueOffset, commitLogOffset, size}，消息体一律从盘上读
    private final InMemoryQueueIndex queueIndex = new InMemoryQueueIndex();
    /**
     * 写入串行化锁。分配 queueOffset → 定位物理偏移 → 编码 → 落盘 → 建索引 必须原子，
     * 否则并发下会出现"索引顺序 ≠ 物理顺序"、queueOffset 与物理位点错位。
     * <p>
     * 粗粒度锁，正确性优先；锁粒度不是本波目标。
     */
    private final Object putMessageLock = new Object();
    /** 上次是否非干净退出（abort 文件仍在）。 */
    private volatile boolean lastShutdownAbnormally = false;
    private volatile long recoveredMessages = 0L;

    /**
     * HA 同步回调 (可选). 设置后, putMessage 成功时会通知 HA 服务.
     */
    private volatile HaAppendCallback haCallback;

    /**
     * 设置 HA 同步回调 (BrokerController 在 start() 时注入).
     */
    public void setHaCallback(HaAppendCallback callback) {
        this.haCallback = callback;
    }

    public CommitLog(final MessageStoreConfig messageStoreConfig) {
        this.messageStoreConfig = messageStoreConfig;
        this.mappedFileQueue = new MappedFileQueue(
                messageStoreConfig.getStorePathCommitLog(),
                messageStoreConfig.getMappedFileSizeCommitLog());

        // 根据刷盘策略选择服务
        if (FlushDiskType.SYNC_FLUSH == messageStoreConfig.getFlushDiskType()) {
            this.flushCommitLogService = new GroupCommitService(this);
        } else {
            this.flushCommitLogService = new FlushRealTimeService(this);
        }
    }

    /**
     * 启动CommitLog
     * <p>
     * 先落 abort 文件（干净关闭路径才会删掉它），再起刷盘线程。
     */
    public void start() {
        createAbortFile();
        this.flushCommitLogService.start();
        log.info("CommitLog started, lastShutdownAbnormally={}", lastShutdownAbnormally);
    }

    /**
     * 关闭CommitLog
     * <p>
     * 顺序：先停刷盘线程（避免和收尾刷盘抢），再把剩余数据 flush 干净，
     * 然后写 checkpoint，最后删 abort。不许丢数据。
     */
    public void shutdown() {
        this.flushCommitLogService.shutdown();
        this.flushCommitLogService.waitForRunning(5000);

        // 剩余数据全部 force 到盘
        this.mappedFileQueue.flush(0);
        for (MappedFile mappedFile : this.mappedFileQueue.getMappedFiles()) {
            mappedFile.setCommittedPosition(mappedFile.getWrotePosition());
        }
        long flushedWhere = this.mappedFileQueue.getFlushedOffset();
        this.mappedFileQueue.setFlushedWhere(flushedWhere);
        writeCheckpoint(flushedWhere);
        deleteAbortFile();
        log.info("CommitLog shutdown, flushedWhere={}", flushedWhere);
    }

    /**
     * 加载CommitLog：恢复文件列表（fileFromOffset 从文件名还原）+ 逐条扫描重建索引与位点。
     */
    public boolean load() {
        long begin = System.currentTimeMillis();
        this.lastShutdownAbnormally = abortFile().exists();
        long checkpoint = readCheckpoint();

        boolean result = this.mappedFileQueue.load();
        if (!result) {
            log.error("load mapped file queue failed, storePath={}", this.mappedFileQueue.getStorePath());
            return false;
        }

        // 当前形态无条件全量扫描 + 逐条 CRC 校验：abort 在不在都走同一条路径，
        // 唯一差别是 lastShutdownAbnormally 标记（干净/非干净两条路径返回同一批消息）。
        long recovered = recover();
        this.recoveredMessages = recovered;

        // 恢复出来的 committed/flushed 各自反映真实状态：committed = 已进 page cache 的写入位置，
        // flushed 只有真正 force 成功才推进（见 MappedFile.flush）。
        for (MappedFile mappedFile : this.mappedFileQueue.getMappedFiles()) {
            mappedFile.setCommittedPosition(mappedFile.getWrotePosition());
        }
        long flushedWhere = this.mappedFileQueue.flush(0);
        writeCheckpoint(flushedWhere);

        log.info("load commit log {}, mappedFiles={}, recovered={} msgs, checkpoint={}, "
                        + "lastShutdownAbnormally={}, cost={}ms",
                true, this.mappedFileQueue.getMappedFileCount(), recovered, checkpoint,
                this.lastShutdownAbnormally, System.currentTimeMillis() - begin);
        return true;
    }

    /**
     * 恢复扫描：逐文件从 0 扫到 wrotePosition，认 V2 magic + CRC。
     * <ul>
     *   <li>BLANK ⇒ 跳到下一个文件（不能当坏数据把整个文件截掉）</li>
     *   <li>magic 不是 V2 / totalSize 越界 / CRC 不符 ⇒ 在该记录起始处写 BLANK 封尾，
     *       {@code wrotePosition} 退到该起始处，停止扫这个文件，后面的文件全部丢弃</li>
     *   <li>CRC 合法 ⇒ 用<b>记录里的 queueOffset</b> 建索引（绝不从物理顺序重新推导）</li>
     * </ul>
     *
     * @return 恢复出的消息条数
     */
    private long recover() {
        long recovered = 0L;
        List<MappedFile> files = new ArrayList<>(this.mappedFileQueue.getMappedFiles());
        for (int i = 0; i < files.size(); i++) {
            MappedFile mappedFile = files.get(i);
            final int fileSize = mappedFile.getFileSize();
            final long fileFromOffset = mappedFile.getFileFromOffset();
            int pos = 0;
            boolean reachedBlank = false;
            boolean cleanFileEnd = false;

            while (pos < fileSize) {
                int headerSize = Math.min(RECOVER_HEADER_SIZE, fileSize - pos);
                if (headerSize < RECOVER_HEADER_SIZE) {
                    // 文件尾部连一个定长区都放不下 ⇒ 不可能有合法记录。
                    // [pos, fileSize) 从来没被写过（append 只在放得下整条记录时才推进位点），
                    // 这是"这个文件到此为止"而不是坏数据 ⇒ 不能借此丢弃后面的文件。
                    cleanFileEnd = true;
                    break;
                }
                ByteBuffer header = mappedFile.sliceForRead(pos, headerSize);
                int magic = MessageCodec.magicOf(header);
                if (magic == 0) {
                    // 从未写过的全零区：这是"上次进程只写到 pos 就停了"的正常形态，不是坏数据
                    cleanFileEnd = true;
                    break;
                }
                if (magic == BLANK_MAGIC_CODE) {
                    // 封尾标记：剩余区属于上一个文件的尾巴，直接进下一个文件
                    pos = fileSize;
                    reachedBlank = true;
                    break;
                }
                int totalSize = MessageCodec.verifyRecord(header, fileSize - pos, fileFromOffset + pos);
                if (totalSize == MessageCodec.INVALID) {
                    break;
                }
                MessageExtBrokerInner msg = MessageCodec.decode(mappedFile.sliceForRead(pos, totalSize));
                if (msg == null) {
                    break;
                }
                // 位点以盘上记录为准
                this.queueIndex.appendEntry(msg.getTopic(), msg.getQueueId(), msg.getQueueOffset(),
                        fileFromOffset + pos, totalSize);
                pos += totalSize;
                recovered++;
            }

            if (reachedBlank || pos == fileSize) {
                mappedFile.setWrotePosition(fileSize);
                continue;
            }

            if (cleanFileEnd) {
                // 扫到的是"文件到此为止"（全零尾巴 / 尾巴不足一条记录头）：
                // 位点停在 pos，下一条新记录直接从那里覆盖写。
                // 既不写 BLANK（那不是坏尾巴，不需要封尾标记），也不丢弃后面的文件
                //（后面的文件里是上一个进程真写下去、CRC 认账的记录）。
                mappedFile.setWrotePosition(pos);
                continue;
            }

            // 扫到了坏尾巴：在 pos 处写 BLANK 封尾（位点仍停在 pos，新记录可以接着覆盖写），
            // 并丢弃后面所有文件
            log.warn("recover stopped at file {} pos {}: truncate {} valid byte(s)",
                    mappedFile.getFileName(), pos, pos);
            mappedFile.markBlankAt(pos, fileFromOffset + fileSize, fileSize - pos);
            mappedFile.setWrotePosition(pos);
            for (int j = files.size() - 1; j > i; j--) {
                MappedFile dropped = this.mappedFileQueue.removeLastMappedFile(true);
                if (dropped != null) {
                    log.warn("recover dropped trailing mapped file {}", dropped.getFileName());
                }
            }
            if (pos == 0) {
                MappedFile dropped = this.mappedFileQueue.removeLastMappedFile(true);
                if (dropped != null) {
                    log.warn("recover dropped headless mapped file {}", dropped.getFileName());
                }
            }
            break;
        }
        return recovered;
    }

    /**
     * 存储消息
     */
    public PutMessageResult putMessage(final MessageExtBrokerInner msg) {
        // 粗粒度锁：分配 queueOffset → 定位物理偏移 → 编码 → 落盘 → 建索引 全在同一把锁里，
        // 正确性优先（锁粒度不是本波目标）。
        synchronized (putMessageLock) {
            return putMessageInLock(msg);
        }
    }

    private PutMessageResult putMessageInLock(final MessageExtBrokerInner msg) {
        final String topic = msg.getTopic();
        if (topic == null || topic.isEmpty()) {
            log.warn("putMessage rejected: empty topic");
            return new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, null);
        }
        msg.setStoreTimestamp(System.currentTimeMillis());
        msg.setBodyCRC(msg.getBody() == null ? 0 : UtilAll.crc32(msg.getBody()));

        final int msgSize = MessageCodec.encodedSize(msg);
        if (msgSize < 0) {
            log.warn("message field too long for V2 record, topic: {}", topic);
            return new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, null);
        }
        if (msgSize > MAX_MESSAGE_SIZE) {
            log.warn("message size exceeded, size: {}", msgSize);
            return new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, null);
        }
        if (msgSize > this.mappedFileQueue.getMappedFileSize()) {
            log.warn("message size {} larger than mappedFileSize {}", msgSize,
                    this.mappedFileQueue.getMappedFileSize());
            return new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, null);
        }

        // queueOffset 在追加之前分配（锁内，与物理顺序严格配对）
        final long queueOffset = this.queueIndex.allocateOffset(topic, msg.getQueueId());
        msg.setQueueOffset(queueOffset);

        AppendMessageResult result = null;
        byte[] encoded = null;
        for (int times = 0; times < 3; times++) {
            MappedFile mappedFile = this.mappedFileQueue.getLastMappedFile();
            if (mappedFile == null) {
                log.error("create mapped file error");
                return new PutMessageResult(PutMessageStatus.CREATE_MAPPED_FILE_FAILED, null);
            }

            final int fileSize = mappedFile.getFileSize();
            final int pos = mappedFile.getWrotePosition();
            if (pos + msgSize > fileSize) {
                // 剩余空间放不下下一条记录 ⇒ 写 BLANK 封尾，新记录从下个文件头开始
                long nextFileOffset = mappedFile.getFileFromOffset() + fileSize;
                if (!mappedFile.fillBlank(pos, nextFileOffset)) {
                    log.warn("no room even for BLANK header, seal file without marker: {}",
                            mappedFile.getFileName());
                }
                mappedFile.setWrotePosition(fileSize);
                continue;
            }

            final long commitLogOffset = mappedFile.getFileFromOffset() + pos;
            encoded = MessageCodec.encode(msg, commitLogOffset);
            if (encoded == null) {
                return new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, null);
            }
            result = mappedFile.appendMessagesInner(encoded, pos);
            if (result.getStatus() == AppendMessageResult.AppendMessageStatus.PUT_OK) {
                msg.setCommitLogOffset(commitLogOffset);
                msg.setStoreSize(encoded.length);
                // 写进 mmap 即已对后续读可见（本波没有独立的 commit 阶段），committed 跟着 wrote 走
                mappedFile.setCommittedPosition(mappedFile.getWrotePosition());
                break;
            }
            if (result.getStatus() == AppendMessageResult.AppendMessageStatus.END_OF_FILE) {
                continue;
            }
            return new PutMessageResult(PutMessageStatus.UNKNOWN_ERROR, result);
        }

        if (result == null || result.getStatus() != AppendMessageResult.AppendMessageStatus.PUT_OK) {
            log.error("append message failed after retries, topic: {}", topic);
            return new PutMessageResult(PutMessageStatus.UNKNOWN_ERROR, result);
        }

        // 根据刷盘策略处理
        final boolean flushOK = handleFlushAndHA(result, encoded);

        // 写入成功后建立位点索引 (供 PullMessageProcessor 查询，消息体从盘上读)
        this.queueIndex.appendEntry(topic, msg.getQueueId(), queueOffset,
                result.getWroteOffset(), encoded.length);
        // 通知 HA 服务 (主从同步): Master 模式会异步推送给所有 Slave
        if (haCallback != null) {
            haCallback.onMessageAppended(result.getWroteOffset(), encoded);
        }

        if (!flushOK) {
            // 记录已经进存储（进程内可读，后续 force / 关机刷盘会落住），但"按 SYNC_FLUSH 策略已落盘"
            // 这件事没成 —— 绝不能对客户报 PUT_OK：那等于盘没落住而客户端显示成功。
            // 状态位必须一路传到客户端（SendMessageProcessor 认这个状态码，见 T2）。
            return new PutMessageResult(PutMessageStatus.FLUSH_DISK_TIMEOUT, result);
        }
        return new PutMessageResult(PutMessageStatus.PUT_OK, result);
    }

    /**
     * 按物理偏移读一条消息（<b>必经盘</b>：findMappedFileByOffset → selectMappedBuffer → decode → release）。
     *
     * @param commitLogOffset 记录的物理起始偏移
     * @return 解码出的消息；偏移越界 / 记录损坏时返回 null
     */
    public MessageExt readMessage(final long commitLogOffset) {
        return readMessage(commitLogOffset, -1);
    }

    private MessageExt readMessage(final long commitLogOffset, final int knownSize) {
        MappedFile mappedFile = this.mappedFileQueue.findMappedFileByOffset(commitLogOffset, false);
        if (mappedFile == null) {
            return null;
        }
        final int relPos = (int) (commitLogOffset - mappedFile.getFileFromOffset());
        SelectMappedBufferResult header = null;
        SelectMappedBufferResult whole = null;
        try {
            int size = knownSize;
            if (size <= 0) {
                header = mappedFile.selectMappedBuffer(relPos, MessageCodec.FIXED_AREA_SIZE);
                if (header == null) {
                    return null;
                }
                size = header.getByteBuffer().getInt(MessageCodec.POS_TOTAL_SIZE);
                int readable = mappedFile.getReadPosition() - relPos;
                if (size < MessageCodec.FIXED_AREA_SIZE || size > readable) {
                    return null;
                }
            }
            whole = mappedFile.selectMappedBuffer(relPos, size);
            if (whole == null) {
                return null;
            }
            return MessageCodec.decode(whole.getByteBuffer());
        } catch (Exception e) {
            log.error("readMessage failed at offset {}", commitLogOffset, e);
            return null;
        } finally {
            if (header != null) {
                header.release();
            }
            if (whole != null) {
                whole.release();
            }
        }
    }

    /**
     * 拉取消息：索引只给位点，消息体一律从 CommitLog 盘上读。
     *
     * @param topic       主题
     * @param queueId     队列
     * @param queueOffset 起始队列偏移
     * @param maxNum      最多条数
     * @return 按 queueOffset 升序的消息列表（读不到的条目会被跳过）
     */
    public List<MessageExt> pullMessage(final String topic, final int queueId,
                                       final long queueOffset, final int maxNum) {
        List<InMemoryQueueIndex.Entry> entries =
                this.queueIndex.queryEntries(topic, queueId, queueOffset, maxNum);
        List<MessageExt> messages = new ArrayList<>(entries.size());
        for (InMemoryQueueIndex.Entry entry : entries) {
            MessageExt msg = readMessage(entry.getCommitLogOffset(), entry.getSize());
            if (msg == null) {
                log.error("pullMessage: record at commitLogOffset={} (queueOffset={}) unreadable, topic={}",
                        entry.getCommitLogOffset(), entry.getQueueOffset(), topic);
                continue;
            }
            messages.add(msg);
        }
        return messages;
    }

    /**
     * 处理刷盘和高可用，并把"这条消息是否按策略真的落住了盘"报给调用方。
     *
     * @return {@code false} = 同步刷盘超时/失败（{@code GroupCommitService} 只在 force 成功时才
     *         {@code wakeupCustomer}，所以这里拿到 false 就是"盘上没落住"的确证），
     *         调用方必须把 {@link PutMessageStatus#FLUSH_DISK_TIMEOUT} 报出去；
     *         异步刷盘不做等待，返回 true（其失败由 {@code shutdown} / 恢复扫描兜住）。
     */
    private boolean handleFlushAndHA(AppendMessageResult result, byte[] encoded) {
        // 同步刷盘：等待刷盘完成
        if (FlushDiskType.SYNC_FLUSH == this.messageStoreConfig.getFlushDiskType()) {
            GroupCommitRequest request = new GroupCommitRequest(result.getWroteOffset() + result.getWroteBytes());
            ((GroupCommitService) this.flushCommitLogService).putRequest(request);
            // 等待刷盘完成（刷盘失败 ⇒ 客户等到 syncFlushTimeout 后拿到 false）
            boolean flushOK = request.waitForFlush(this.messageStoreConfig.getSyncFlushTimeout());
            if (!flushOK) {
                log.error("sync flush message timeout, nextOffset={} —— 本条按 FLUSH_DISK_TIMEOUT 上报",
                        request.getNextOffset());
                return false;
            }
        }
        // 异步刷盘由 FlushRealTimeService 定时处理
        return true;
    }

    /**
     * 同步刷盘入口：对<b>包含</b> {@code nextOffset} 的那个文件 force，成功才返回 true。
     */
    boolean flushBeyondOffset(final long nextOffset) {
        for (MappedFile mappedFile : this.mappedFileQueue.getMappedFiles()) {
            final long fileFrom = mappedFile.getFileFromOffset();
            final long fileTo = fileFrom + mappedFile.getFileSize();
            if (nextOffset > fileFrom && nextOffset <= fileTo) {
                if (!mappedFile.flush(0)) {
                    return false;
                }
                mappedFile.setCommittedPosition(mappedFile.getWrotePosition());
                long flushedWhere = this.mappedFileQueue.getFlushedOffset();
                this.mappedFileQueue.setFlushedWhere(flushedWhere);
                writeCheckpoint(flushedWhere);
                return flushedWhere >= nextOffset;
            }
        }
        log.warn("flushBeyondOffset: no mapped file contains nextOffset={}", nextOffset);
        return false;
    }

    // ==================== checkpoint / abort ====================

    private File checkpointFile() {
        return new File(this.messageStoreConfig.getStorePathRootDir(), CHECKPOINT_FILE_NAME);
    }

    private File abortFile() {
        return new File(this.messageStoreConfig.getStorePathRootDir(), ABORT_FILE_NAME);
    }

    /** 写 checkpoint（8 字节大端 long = 已确认落盘的最大连续物理偏移），临时文件 + rename 保证原子。 */
    void writeCheckpoint(final long flushedWhere) {
        File target = checkpointFile();
        File tmp = new File(target.getAbsolutePath() + ".tmp");
        RandomAccessFile raf = null;
        try {
            target.getParentFile().mkdirs();
            raf = new RandomAccessFile(tmp, "rw");
            raf.setLength(0);
            raf.writeLong(flushedWhere);
            raf.getFD().sync();
            raf.close();
            raf = null;
            try {
                Files.move(tmp.toPath(), target.toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException atomicUnsupported) {
                // 个别文件系统不支持 ATOMIC_MOVE，退化成普通替换移动
                Files.move(tmp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error("write checkpoint failed: {}", target.getAbsolutePath(), e);
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
            if (tmp.exists()) {
                boolean ignored = tmp.delete();
            }
        }
    }

    /** @return 已确认落盘的最大连续物理偏移；文件不存在/读失败返回 -1 */
    public long readCheckpoint() {
        File file = checkpointFile();
        if (!file.exists()) {
            return -1L;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            if (raf.length() < 8) {
                return -1L;
            }
            return raf.readLong();
        } catch (Exception e) {
            log.warn("read checkpoint failed: {}", file.getAbsolutePath(), e);
            return -1L;
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }

    private void createAbortFile() {
        try {
            File file = abortFile();
            file.getParentFile().mkdirs();
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            try {
                raf.write("abort".getBytes(StandardCharsets.UTF_8));
            } finally {
                raf.close();
            }
        } catch (Exception e) {
            log.error("create abort file failed", e);
        }
    }

    private void deleteAbortFile() {
        File file = abortFile();
        if (file.exists() && !file.delete()) {
            log.warn("delete abort file failed: {}", file.getAbsolutePath());
        }
    }

    /** 上次是否非干净退出（abort 文件在启动时仍存在）。 */
    public boolean isLastShutdownAbnormally() {
        return lastShutdownAbnormally;
    }

    /** 本次 load 恢复出的消息条数。 */
    public long getRecoveredMessages() {
        return recoveredMessages;
    }

    /**
     * 获取MappedFile队列
     */
    public MappedFileQueue getMappedFileQueue() {
        return mappedFileQueue;
    }

    /**
     * 获取进程内 Queue <b>位点</b>索引 — 只提供 {queueOffset, commitLogOffset, size}；
     * 消息内容请用 {@link #pullMessage(String, int, long, int)}（必经盘）。
     */
    public InMemoryQueueIndex getQueueIndex() {
        return queueIndex;
    }

    /** 队列里已写入的最大物理偏移。 */
    public long getMaxPhyOffset() {
        return this.mappedFileQueue.getMaxOffset();
    }
}

// AppendMessageResult, PutMessageResult, PutMessageStatus, MessageExtBrokerInner
// moved to com.taotao.cloud.mq.consistency.ttc.store.*

// MessageStoreConfig moved to its own file: com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig
// FlushDiskType moved to its own file: com.taotao.cloud.mq.consistency.ttc.store.log.FlushDiskType

// MessageExtBrokerInner moved to com.taotao.cloud.mq.consistency.ttc.store.MessageExtBrokerInner (public class).

/**
 * 刷盘服务基类
 */
abstract class FlushCommitLogService extends ServiceThread {
    protected static final int RETRY_TIMES_OVER = 3;

    protected final CommitLog commitLog;

    protected FlushCommitLogService(CommitLog commitLog) {
        this.commitLog = commitLog;
    }
}

/**
 * 同步刷盘服务 - GroupCommit
 */
class GroupCommitService extends FlushCommitLogService {
    private static final Logger log = LoggerFactory.getLogger(GroupCommitService.class);
    // 待提交请求队列
    private final java.util.concurrent.LinkedBlockingQueue<GroupCommitRequest> requestQueue =
            new java.util.concurrent.LinkedBlockingQueue<>();

    GroupCommitService(CommitLog commitLog) {
        super(commitLog);
    }

    public void putRequest(GroupCommitRequest request) {
        requestQueue.offer(request);
    }

    @Override
    public void run() {
        while (!this.isStopped()) {
            try {
                GroupCommitRequest request = requestQueue.take();
                // 执行刷盘
                doCommit(request);
            } catch (InterruptedException e) {
                // 清掉中断标记：带着它调 force() 会抛 ClosedByInterruptException 并永久关掉 channel
                Thread.interrupted();
                break;
            }
        }
        // 退出前排干残留请求，但绝不谎报刷盘成功：不 wakeup，让客户端自己超时
    }

    private void doCommit(GroupCommitRequest request) {
        boolean ok;
        try {
            // 对包含 nextOffset 的那个文件真 force(false)
            ok = this.commitLog.flushBeyondOffset(request.getNextOffset());
        } catch (Exception e) {
            log.error("GroupCommitService doCommit failed", e);
            ok = false;
        }
        if (ok) {
            // 只有真的落盘成功才通知客户；失败 ⇒ flushOK 保持 false，客户超时后拿到 false
            request.wakeupCustomer();
        }
    }

    @Override
    public String getServiceName() {
        return "GroupCommitService";
    }
}

/**
 * 异步刷盘服务
 */
class FlushRealTimeService extends FlushCommitLogService {
    private static final long FLUSH_INTERVAL_MILLIS = 1000L;

    FlushRealTimeService(CommitLog commitLog) {
        super(commitLog);
    }

    @Override
    public void run() {
        while (!this.isStopped()) {
            try {
                Thread.sleep(FLUSH_INTERVAL_MILLIS);
                // 执行刷盘
                doFlush();
            } catch (InterruptedException e) {
                // 清掉中断标记后再做收尾刷盘：带着它调 force() 会抛
                // ClosedByInterruptException 并永久关掉 FileChannel（数据就再也刷不出去了）
                Thread.interrupted();
                break;
            }
        }
        // 停线程前最后再刷一次，保证不丢数据
        doFlush();
    }

    private void doFlush() {
        // 真刷：逐个文件 force(false)，并把 flushedWhere 推进到连续已落盘前缀
        MappedFileQueue queue = this.commitLog.getMappedFileQueue();
        long flushedWhere = queue.flush(0);
        queue.setFlushedWhere(flushedWhere);
        this.commitLog.writeCheckpoint(flushedWhere);
    }

    @Override
    public String getServiceName() {
        return "FlushRealTimeService";
    }
}

/**
 * GroupCommit请求
 */
class GroupCommitRequest {
    private final long nextOffset;
    private final java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
    private volatile boolean flushOK = false;

    public GroupCommitRequest(long nextOffset) {
        this.nextOffset = nextOffset;
    }

    public long getNextOffset() {
        return nextOffset;
    }

    public void wakeupCustomer() {
        this.flushOK = true;
        this.countDownLatch.countDown();
    }

    public boolean waitForFlush(long timeoutMillis) {
        try {
            this.countDownLatch.await(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
            return this.flushOK;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}

// PutMessageResult moved to com.taotao.cloud.mq.consistency.ttc.store.PutMessageResult

/**
 * 工具类
 */
class UtilAll {
    public static int crc32(byte[] data) {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(data);
        return (int) crc.getValue();
    }
}

/**
 * 服务线程基类
 */
abstract class ServiceThread implements Runnable {
    protected volatile boolean stopped = false;
    protected Thread thread;

    public void start() {
        this.thread = new Thread(this, getServiceName());
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public void shutdown() {
        this.stopped = true;
        if (this.thread != null) {
            this.thread.interrupt();
        }
    }

    /**
     * 等线程真正退出（关闭路径用它保证"先停刷盘线程，再收尾刷盘"）。
     */
    public void waitForRunning(long interval) {
        Thread t = this.thread;
        if (t != null && t.isAlive()) {
            try {
                t.join(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public abstract String getServiceName();
}
