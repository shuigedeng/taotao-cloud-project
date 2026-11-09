package com.taotao.cloud.mq.consistency.ttc.store.log;

import com.taotao.cloud.mq.consistency.ttc.store.AppendMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.log.ReferenceResource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存映射文件
 * 提供文件的内存映射访问，支持顺序读写
 */
public class MappedFile extends ReferenceResource {

    // OS页大小 4KB
    public static final int OS_PAGE_SIZE = 1024 * 4;
    private static final Logger log = LoggerFactory.getLogger(MappedFile.class);
    // 总映射虚拟内存
    private static final AtomicLong TOTAL_MAPPED_VIRTUAL_MEMORY = new AtomicLong(0);

    // 总映射文件数
    private static final AtomicInteger TOTAL_MAPPED_FILES = new AtomicInteger(0);

    // 当前写入位置
    protected final AtomicInteger wrotePosition = new AtomicInteger(0);

    // 提交位置（用于异步刷盘）
    protected final AtomicInteger committedPosition = new AtomicInteger(0);

    // 刷盘位置
    private final AtomicInteger flushedPosition = new AtomicInteger(0);

    // 文件大小
    protected int fileSize;
    // 文件起始偏移量
    protected long fileFromOffset = 0;
    // 文件通道
    private FileChannel fileChannel;
    /**
     * 打开该通道的 RandomAccessFile，必须一直被引用住：
     * 老写法 {@code new RandomAccessFile(f,"rw").getChannel()} 不留引用，RAF 被 GC finalize 之后
     * channel 会被顺手关掉，随后 force() 抛异常、盘上的映射也再刷不出去。
     */
    private RandomAccessFile fileDescriptor;
    // 内存映射缓冲区
    private MappedByteBuffer mappedByteBuffer;
    // 文件名称
    private String fileName;
    // 文件对象
    private File file;
    // 是否是队列中第一个创建的文件
    private boolean firstCreateInQueue = false;

    // 最后修改时间戳
    private long lastModifiedTimestamp = System.currentTimeMillis();

    // 存储时间戳
    private long storeTimestamp = System.currentTimeMillis();

    public MappedFile() {
    }

    public MappedFile(final String fileName, final int fileSize) throws IOException {
        init(fileName, fileSize);
    }

    /**
     * 清理MappedByteBuffer
     */
    public static void cleanMappedByteBuffer(final MappedByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect()) {
            return;
        }
        try {
            java.lang.reflect.Method cleanerMethod = buffer.getClass().getMethod("cleaner");
            cleanerMethod.setAccessible(true);
            Object cleaner = cleanerMethod.invoke(buffer);
            if (cleaner != null) {
                java.lang.reflect.Method cleanMethod = cleaner.getClass().getMethod("clean");
                cleanMethod.invoke(cleaner);
            }
        } catch (Exception e) {
            log.warn("clean MappedByteBuffer failed", e);
        }
    }

    /**
     * 获取总映射虚拟内存
     */
    public static long getTotalMappedVirtualMemory() {
        return TOTAL_MAPPED_VIRTUAL_MEMORY.get();
    }

    /**
     * 获取总映射文件数
     */
    public static int getTotalMappedFiles() {
        return TOTAL_MAPPED_FILES.get();
    }

    /**
     * 初始化文件
     */
    public void init(final String fileName, final int fileSize) throws IOException {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);

        // 确保目录存在
        this.file.getParentFile().mkdirs();

        // 创建RandomAccessFile
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(this.file, "rw");
            randomAccessFile.setLength(fileSize);
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }

        // 创建FileChannel（RandomAccessFile 必须留在字段里持有，见 fileDescriptor 注释）
        this.fileDescriptor = new RandomAccessFile(this.file, "rw");
        this.fileChannel = this.fileDescriptor.getChannel();

        // 创建内存映射
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

        // 更新统计信息
        TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(fileSize);
        TOTAL_MAPPED_FILES.incrementAndGet();

        log.info("MappedFile initialized: {}, size: {}", fileName, fileSize);
    }

    /**
     * 追加消息
     */
    public boolean appendMessage(final byte[] data) {
        return appendMessage(data, 0, data.length);
    }

    /**
     * 追加消息（指定偏移和长度）
     * <p>
     * 位点用 CAS 抢占，字节用<b>绝对写</b>（duplicate 后定位），不再依赖 mappedByteBuffer 自带的
     * position —— 老实现把 {@code mappedByteBuffer.put(...)} 的相对写和单独推进的 wrotePosition 混用，
     * 新文件从 0 开始时恰好一致，load 之后必然脱钩。
     */
    public boolean appendMessage(final byte[] data, final int offset, final int length) {
        while (true) {
            int currentPos = this.wrotePosition.get();
            if (currentPos + length > this.fileSize) {
                log.warn("append message failed, fileSize: {}, currentPos: {}, length: {}",
                        this.fileSize, currentPos, length);
                return false;
            }
            if (false == this.wrotePosition.compareAndSet(currentPos, currentPos + length)) {
                // 位置被别的写者抢走，重来
                continue;
            }
            try {
                putBytes(currentPos, data, offset, length);
                return true;
            } catch (Exception e) {
                log.error("append message error", e);
                return false;
            }
        }
    }

    /**
     * 在<b>指定位置</b>追加一段字节（CommitLog 在粗粒度锁下先定位、再编码、最后落盘）。
     *
     * @param data        数据
     * @param expectedPos 期望的记录起始位置（文件内相对偏移）
     * @return 追加结果；空间不足返回 {@code END_OF_FILE}，位置被抢占返回 {@code UNKNOWN_ERROR}
     */
    public AppendMessageResult appendMessagesInner(final byte[] data, final int expectedPos) {
        final long now = System.currentTimeMillis();
        final int length = data.length;

        if (expectedPos < 0 || expectedPos + length > this.fileSize) {
            return new AppendMessageResult(AppendMessageResult.AppendMessageStatus.END_OF_FILE,
                    this.fileFromOffset + this.wrotePosition.get(), 0, now);
        }

        if (false == this.wrotePosition.compareAndSet(expectedPos, expectedPos + length)) {
            // 定位与落盘之间位点被人动过：让调用方重新定位（本波 CommitLog 全程持锁，正常不会走到）
            log.error("appendMessagesInner position raced, file: {}, expectedPos: {}, wrotePosition: {}",
                    this.fileName, expectedPos, this.wrotePosition.get());
            return new AppendMessageResult(AppendMessageResult.AppendMessageStatus.UNKNOWN_ERROR,
                    this.fileFromOffset + expectedPos, 0, now);
        }

        try {
            putBytes(expectedPos, data, 0, length);
        } catch (Exception e) {
            log.error("append message error, file: {}", this.fileName, e);
            return new AppendMessageResult(AppendMessageResult.AppendMessageStatus.UNKNOWN_ERROR,
                    this.fileFromOffset + expectedPos, 0, now);
        }

        this.lastModifiedTimestamp = now;
        this.storeTimestamp = now;
        return new AppendMessageResult(AppendMessageResult.AppendMessageStatus.PUT_OK,
                this.fileFromOffset + expectedPos, length, now);
    }

    /**
     * 用 BLANK 记录给当前文件封尾：在 {@code fromPos} 处按 {@link #putBlankHeader} 的 16 字节布局写封尾标记，
     * 并把 wrotePosition 推到文件末尾。
     * <p>
     * 恢复扫描读到 BLANK 必须跳到下一个文件，不能当成坏数据把整个文件截掉。
     * <p>
     * 写入侧的 {@code [fromPos, fileSize)} <b>从来没被写过</b>（append 只在放得下整条记录时才推进位点，
     * mapped 区在 {@link #init} 里已 {@code setLength(fileSize)} 预零），所以这里<b>只需要 16 字节头</b>，
     * 不需要清尾巴 —— 那是 {@link #markBlankAt}（恢复侧）才要做的事。
     *
     * @return 剩余空间连 BLANK 头（{@value #BLANK_HEADER_SIZE} 字节）都放不下时返回 false（此时只推进位点，不留封尾标记）
     */
    public boolean fillBlank(final int fromPos, final long nextFileOffset) {
        final int blankLen = this.fileSize - fromPos;
        if (fromPos < 0 || blankLen < BLANK_HEADER_SIZE) {
            return false;
        }
        if (false == this.wrotePosition.compareAndSet(fromPos, this.fileSize)) {
            log.error("fillBlank raced, file: {}, fromPos: {}", this.fileName, fromPos);
            return false;
        }
        putBlankHeader(fromPos, nextFileOffset, blankLen);
        return true;
    }

    /**
     * 只写 16 字节 BLANK 头，<b>不动</b> wrotePosition、不清尾巴。
     * <p>
     * 布局与真记录对齐，这样扫描器在读同一个偏移（4）就能认出它：
     * {@code [0..3] 尾巴总长 int | [4..7] BLANK_MAGIC_CODE int | [8..15] 下一文件起始偏移 long | 其余填 0}
     */
    private void putBlankHeader(final int fromPos, final long nextFileOffset, final int blankLen) {
        byte[] header = new byte[BLANK_HEADER_SIZE];
        ByteBuffer tmp = ByteBuffer.wrap(header);
        tmp.putInt(blankLen);
        tmp.putInt(CommitLog.BLANK_MAGIC_CODE);
        tmp.putLong(nextFileOffset);
        putBytes(fromPos, header, 0, BLANK_HEADER_SIZE);
    }

    /**
     * 恢复扫描用的封尾：写 BLANK 头 + 把尾巴清零，<b>不动</b> wrotePosition —— 它要把位点留在截断点，
     * 但同时也必须在截断处留下 BLANK，让下次扫描认出"这里往后是尾巴"。
     * <p>
     * 两件事缺一不可，且都<b>不许按 {@code blankLen} 长度分配堆数组</b>：
     * <ol>
     *   <li><b>写 16 字节 BLANK 头</b>：给扫描器认出封尾用。</li>
     *   <li><b>把 {@code [fromPos+16, fromPos+blankLen)} 真的清零</b>：截断点之后可能还留着上一个进程
     *       写过、CRC 也认账的<b>陈旧记录</b>。新记录从截断点覆盖写下去之后，那些陈旧字节就紧接在新记录
     *       后面；下次扫描沿着"合法记录 → 下一条"走过去会把它们<b>复活</b>
     *       （实测：500 条里截断第 250 条再补写 1 条，不擦尾巴时重启恢复出 500 条而不是 251 条 ——
     *       见 {@code CommitLogRecoveryTest#testCorruptedRecordInMiddleCutsEverythingAfterIt}）。
     *       "尾巴本来就是 0"只在干净重启时成立，而那条路径已经不再走到这里
     *       （见 {@code CommitLog.recover} 的 {@code cleanFileEnd} 分支）。</li>
     * </ol>
     * 清零用一块固定的 {@link #BLANK_ZERO_CHUNK} 缓冲区循环写，<b>分配量与尾巴长度无关</b>：
     * 老写法 {@code new byte[blankLen]} 在恢复期（{@code CommitLog.recover} 传的是 {@code fileSize - pos}、
     * 默认 {@code mappedFileSizeCommitLog = 1GiB}）一次就是 ~1.07GB 堆，broker 的 fork 堆只有
     * {@code -Xmx2048m}，第二次重启就 {@code OutOfMemoryError}。
     *
     * @param fromPos        封尾位置（文件内相对偏移）
     * @param nextFileOffset 下一个文件的起始物理偏移
     * @param blankLen       尾巴总长（写进 BLANK 头的"总长"字段）
     */
    public void markBlankAt(final int fromPos, final long nextFileOffset, final int blankLen) {
        if (fromPos < 0 || blankLen < BLANK_HEADER_SIZE || fromPos + blankLen > this.fileSize) {
            return;
        }
        putBlankHeader(fromPos, nextFileOffset, blankLen);

        // 尾巴清零：在 duplicate 出来的缓冲区上相对写（有自己的 position，不会移动 mappedByteBuffer
        // 自身的 position —— selectMappedBuffer 的 slice() 依赖这一点），一块小缓冲区循环到底。
        final int fillLen = blankLen - BLANK_HEADER_SIZE;
        if (fillLen > 0) {
            ByteBuffer tail = this.mappedByteBuffer.duplicate();
            tail.position(fromPos + BLANK_HEADER_SIZE);
            int remaining = fillLen;
            while (remaining > 0) {
                int n = remaining < BLANK_ZERO_CHUNK.length ? remaining : BLANK_ZERO_CHUNK.length;
                tail.put(BLANK_ZERO_CHUNK, 0, n);
                remaining -= n;
            }
        }
    }

    /** BLANK 头部长度：尾巴总长(4) + magic(4) + 下一文件起始偏移(8)。 */
    public static final int BLANK_HEADER_SIZE = 16;

    /**
     * 清尾巴用的零缓冲区：大小与尾巴长度无关，恢复期的堆分配因此有界（见 {@link #markBlankAt}）。
     */
    private static final byte[] BLANK_ZERO_CHUNK = new byte[64 * 1024];

    /**
     * 绝对写：duplicate 出来的缓冲区与原映射共享内存但有自己的 position，
     * 因此永远不会移动 mappedByteBuffer 自身的 position（selectMappedBuffer 的 slice() 依赖这一点）。
     */
    private void putBytes(final int pos, final byte[] data, final int offset, final int length) {
        ByteBuffer dup = this.mappedByteBuffer.duplicate();
        dup.position(pos);
        dup.put(data, offset, length);
    }

    /**
     * 从文件内 {@code pos} 处绝对读 {@code length} 字节，不改动任何 position。
     */
    public ByteBuffer sliceForRead(final int pos, final int length) {
        ByteBuffer dup = this.mappedByteBuffer.duplicate();
        dup.position(pos);
        dup.limit(pos + length);
        return dup.slice();
    }

    /**
     * 获取文件大小
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * 获取文件通道
     */
    public FileChannel getFileChannel() {
        return fileChannel;
    }

    /**
     * 获取写入位置
     */
    public int getWrotePosition() {
        return wrotePosition.get();
    }

    /**
     * 设置写入位置
     */
    public void setWrotePosition(int pos) {
        this.wrotePosition.set(pos);
    }

    /**
     * 获取刷盘位置
     */
    public int getFlushedPosition() {
        return flushedPosition.get();
    }

    /**
     * 设置刷盘位置
     */
    public void setFlushedPosition(int pos) {
        this.flushedPosition.set(pos);
    }

    /**
     * 获取提交位置
     */
    public int getCommittedPosition() {
        return this.committedPosition.get();
    }

    /**
     * 设置提交位置
     */
    public void setCommittedPosition(int pos) {
        this.committedPosition.set(pos);
    }

    /**
     * 获取文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 获取MappedByteBuffer
     */
    public MappedByteBuffer getMappedByteBuffer() {
        return mappedByteBuffer;
    }

    /**
     * 获取当前可读位置
     */
    public int getReadPosition() {
        return this.wrotePosition.get();
    }

    /**
     * 刷盘。
     * <p>真正调用 {@code FileChannel.force(false)} 把内存映射区的脏页落到磁盘，
     * <b>只有 force 成功</b>才把 flushedPosition 推进到当前可读位置（wrotePosition）。
     * force 失败时 flushedPosition 保持不动，调用方可以重试。
     *
     * @param flushLeastPages 至少积攒多少页才真正刷盘；0 表示有增量就刷
     * @return 本次刷盘是否成功（没有需要刷的数据也算成功）
     */
    public boolean flush(final int flushLeastPages) {
        if (false == this.isAbleToFlush(flushLeastPages)) {
            return true;
        }

        if (this.fileChannel == null || !this.fileChannel.isOpen()) {
            log.error("flush failed, file channel not available: {}", this.fileName);
            return false;
        }

        if (false == this.hold()) {
            log.warn("flush failed, hold() refused (resource is shutting down): {}", this.fileName);
            return false;
        }

        try {
            final int value = getReadPosition();
            if (value <= this.flushedPosition.get()) {
                return true;
            }
            try {
                // 真正的 fsync：force(false) 只刷数据，不刷元数据
                this.fileChannel.force(false);
            } catch (Exception e) {
                log.error("force flush error, file: {}", this.fileName, e);
                return false;
            }
            this.flushedPosition.set(value);
            return true;
        } finally {
            this.release();
        }
    }

    /**
     * 是否可以刷盘
     */
    private boolean isAbleToFlush(final int flushLeastPages) {
        int flush = this.flushedPosition.get();
        int write = getReadPosition();

        if (this.isFull()) {
            return true;
        }

        if (flushLeastPages > 0) {
            return ((write / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE)) >= flushLeastPages;
        }

        return write > flush;
    }

    /**
     * 是否写满
     */
    public boolean isFull() {
        return this.fileSize == this.wrotePosition.get();
    }

    /**
     * 选择映射缓冲区
     */
    public SelectMappedBufferResult selectMappedBuffer(int pos, int size) {
        int readPosition = getReadPosition();
        if (pos + size <= readPosition && pos >= 0) {
            if (this.hold()) {
                ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
                byteBuffer.position(pos);
                ByteBuffer byteBufferNew = byteBuffer.slice();
                byteBufferNew.limit(size);
                return new SelectMappedBufferResult(this.fileFromOffset + pos, byteBufferNew, size, this);
            }
        }
        return null;
    }

    /**
     * 选择映射缓冲区（从指定位置到末尾）
     */
    public SelectMappedBufferResult selectMappedBuffer(int pos) {
        int readPosition = getReadPosition();
        if (pos < readPosition && pos >= 0) {
            if (this.hold()) {
                ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
                byteBuffer.position(pos);
                int size = readPosition - pos;
                ByteBuffer byteBufferNew = byteBuffer.slice();
                byteBufferNew.limit(size);
                return new SelectMappedBufferResult(this.fileFromOffset + pos, byteBufferNew, size, this);
            }
        }
        return null;
    }

    /**
     * 获取文件起始偏移量
     */
    public long getFileFromOffset() {
        return fileFromOffset;
    }

    /**
     * 设置文件起始偏移量
     */
    public void setFileFromOffset(long fileFromOffset) {
        this.fileFromOffset = fileFromOffset;
    }

    /**
     * 销毁文件
     */
    public boolean destroy(final long intervalForcibly) {
        this.shutdown(intervalForcibly);

        if (this.isCleanupOver()) {
            try {
                this.fileChannel.close();
                log.info("close file channel " + this.fileName + " OK");

                long beginTime = System.currentTimeMillis();
                boolean result = this.file.delete();
                log.info("delete file[{}] " + (result ? "OK, " : "Failed, ") + "cost={}ms",
                        this.fileName, System.currentTimeMillis() - beginTime);
            } catch (Exception e) {
                log.warn("destroy file " + this.fileName + " failed. ", e);
            }
            return true;
        } else {
            log.warn("destroy mapped file[{}]failed, cleanupOver:{}.",
                    fileName, isCleanupOver());
            return false;
        }
    }

    @Override
    public boolean cleanup(long currentRef) {
        if (this.isAvailable()) {
            log.error("this file[{}] have not shutdown, ignore cleanup.", this.fileName);
            return false;
        }

        if (this.isCleanupOver()) {
            log.error("this file[{}] have cleanup, do not do it again.", this.fileName);
            return true;
        }

        cleanMappedByteBuffer(this.mappedByteBuffer);
        TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(this.fileSize * (-1));
        TOTAL_MAPPED_FILES.decrementAndGet();
        log.info("cleanup file[{}]  OK, cost: {} ms", this.fileName, 0);
        return true;
    }

    /**
     * 获取引用计数
     */
    public long getRefCount() {
        return this.refCount.get();
    }

    /**
     * 是否是队列中第一个创建的文件
     */
    public boolean isFirstCreateInQueue() {
        return firstCreateInQueue;
    }

    /**
     * 设置是否是队列中第一个创建的文件
     */
    public void setFirstCreateInQueue(boolean firstCreateInQueue) {
        this.firstCreateInQueue = firstCreateInQueue;
    }

    /**
     * 获取最后修改时间戳
     */
    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    /**
     * 获取存储时间戳
     */
    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    /**
     * 设置存储时间戳
     */
    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
    }

    /**
     * 预热文件
     */
    public void warmUp() {
        // 简单实现：顺序写入每个页的第一个字节
        int pageSize = OS_PAGE_SIZE;
        for (int i = 0; i < this.fileSize; i += pageSize) {
            this.mappedByteBuffer.put(i, (byte) 0);
        }
        this.wrotePosition.set(0);
    }
}

/**
 * 选择映射缓冲区结果
 */
class SelectMappedBufferResult {
    private final long startOffset;
    private final ByteBuffer byteBuffer;
    private final int size;
    private final MappedFile mappedFile;

    public SelectMappedBufferResult(long startOffset, ByteBuffer byteBuffer, int size, MappedFile mappedFile) {
        this.startOffset = startOffset;
        this.byteBuffer = byteBuffer;
        this.size = size;
        this.mappedFile = mappedFile;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public int getSize() {
        return size;
    }

    public MappedFile getMappedFile() {
        return mappedFile;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (this.mappedFile != null) {
            this.mappedFile.release();
        }
    }
}
