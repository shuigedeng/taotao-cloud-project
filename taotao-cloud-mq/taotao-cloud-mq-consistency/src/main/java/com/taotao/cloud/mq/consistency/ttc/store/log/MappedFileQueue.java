package com.taotao.cloud.mq.consistency.ttc.store.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MappedFile队列管理
 * 管理多个MappedFile，提供顺序访问能力
 */
public class MappedFileQueue {

    private static final Logger log = LoggerFactory.getLogger(MappedFileQueue.class);

    // 存储目录
    private final String storePath;

    // 单个文件大小
    private final int mappedFileSize;

    // MappedFile列表
    private final CopyOnWriteArrayList<MappedFile> mappedFiles = new CopyOnWriteArrayList<>();

    // 已刷盘位置
    private volatile long flushedWhere = 0;

    // 已提交位置（异步刷盘时使用）
    private volatile long committedWhere = 0;

    // 目录是否已经加载过（load 幂等）
    private volatile boolean loaded = false;

    public MappedFileQueue(final String storePath, final int mappedFileSize) {
        this.storePath = storePath;
        this.mappedFileSize = mappedFileSize;
        // 不在构造函数里自调 load()：老实现这里 load() 一次、CommitLog.load() 再 load() 一次，
        // 同一个目录被加载两遍。统一由 CommitLog.load() 负责（并做恢复扫描）。
    }

    /**
     * 把目录下已有的 CommitLog 文件挂进队列。
     * <p>
     * 文件名是 {@code String.format("%020d", fileFromOffset)}，所以 fileFromOffset 必须从文件名恢复
     * （老实现从不恢复，恒为 0）。每个文件的 wrote/flushed/committed 一律<b>从 0 开始</b>，
     * 由 {@code CommitLog.load()} 的逐条扫描把 wrotePosition 推到"最后一条合法记录之后"——
     * 老实现直接把三个位点设成整个文件大小，重启后位点全是假的。
     */
    public synchronized boolean load() {
        if (this.loaded) {
            return true;
        }
        File dir = new File(this.storePath);
        if (!dir.exists()) {
            dir.mkdirs();
            this.loaded = true;
            return true;
        }

        if (!dir.isDirectory()) {
            log.error("{} is not a directory", this.storePath);
            return false;
        }

        // 获取所有文件并按名称排序（文件名是 20 位零填充的十进制偏移，字典序 == 数值序）
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            for (File file : files) {
                if (!file.isFile()) {
                    continue;
                }
                long fileFromOffset;
                try {
                    fileFromOffset = Long.parseLong(file.getName());
                } catch (NumberFormatException e) {
                    log.warn("ignore not-a-commitlog file {}", file.getAbsolutePath());
                    continue;
                }
                if (fileFromOffset < 0 || fileFromOffset % this.mappedFileSize != 0) {
                    log.warn("ignore misaligned commitlog file {}, offset {}", file.getAbsolutePath(), fileFromOffset);
                    continue;
                }
                try {
                    MappedFile mappedFile = new MappedFile(file.getAbsolutePath(), this.mappedFileSize);
                    mappedFile.setFileFromOffset(fileFromOffset);
                    mappedFile.setWrotePosition(0);
                    mappedFile.setFlushedPosition(0);
                    mappedFile.setCommittedPosition(0);
                    this.mappedFiles.add(mappedFile);
                    log.info("load mapped file {} success, fileFromOffset={}", file.getAbsolutePath(), fileFromOffset);
                } catch (IOException e) {
                    log.error("load mapped file {} failed", file.getAbsolutePath(), e);
                    return false;
                }
            }
        }
        this.loaded = true;

        return true;
    }

    /**
     * 逐文件刷盘，并把 flushedWhere 推进到"连续已落盘前缀"的末尾。
     */
    public long flush(final int flushLeastPages) {
        Object[] mfs = this.mappedFiles.toArray();
        for (Object mf : mfs) {
            ((MappedFile) mf).flush(flushLeastPages);
        }
        long flushed = getFlushedOffset();
        this.flushedWhere = flushed;
        return flushed;
    }

    /**
     * 刷某个文件（同步刷盘用：只 force 包含目标偏移的那个文件）。
     */
    public boolean flushFile(final MappedFile mappedFile, final int flushLeastPages) {
        return mappedFile.flush(flushLeastPages);
    }

    /**
     * 队列已确认落盘的最大物理偏移。
     */
    public long getFlushedWhereOffset() {
        return this.flushedWhere;
    }

    /**
     * 最后一个文件（不创建）。
     */
    public MappedFile peekLastMappedFile() {
        CopyOnWriteArrayList<MappedFile> files = this.mappedFiles;
        if (files.isEmpty()) {
            return null;
        }
        return files.get(files.size() - 1);
    }

    /**
     * 从队列尾部摘掉一个文件（恢复扫描发现尾部坏记录时用）。
     *
     * @param destroyOnRemove true 表示同时把盘上文件删掉
     */
    public MappedFile removeLastMappedFile(final boolean destroyOnRemove) {
        MappedFile last = peekLastMappedFile();
        if (last == null) {
            return null;
        }
        this.mappedFiles.remove(last);
        if (destroyOnRemove) {
            last.destroy(1000);
        } else {
            last.shutdown(1000);
        }
        return last;
    }

    /**
     * 队列中所有文件的物理总长度（不创建文件）。
     */
    public long getTotalMappedFileSize() {
        long total = 0;
        for (MappedFile mappedFile : this.mappedFiles) {
            total += mappedFile.getFileSize();
        }
        return total;
    }

    /**
     * 获取最后一个MappedFile，如果不存在或已满则创建新的
     */
    public MappedFile getLastMappedFile(final long startOffset, boolean createIfNotExists) {
        MappedFile mappedFileLast = peekLastMappedFile();

        // 如果最后一个文件已满或不存在，创建新文件
        if (mappedFileLast == null || mappedFileLast.isFull()) {
            if (createIfNotExists) {
                long fileOffset;
                if (mappedFileLast == null) {
                    fileOffset = startOffset - (startOffset % this.mappedFileSize);
                } else {
                    fileOffset = mappedFileLast.getFileFromOffset() + this.mappedFileSize;
                }

                String fileName = this.storePath + File.separator
                        + String.format("%020d", fileOffset);

                try {
                    MappedFile mappedFile = new MappedFile(fileName, this.mappedFileSize);
                    mappedFile.setFileFromOffset(fileOffset);
                    this.mappedFiles.add(mappedFile);
                    return mappedFile;
                } catch (IOException e) {
                    log.error("create mapped file {} failed", fileName, e);
                }
            }
        }

        return mappedFileLast;
    }

    /**
     * 获取最后一个MappedFile
     */
    public MappedFile getLastMappedFile() {
        return getLastMappedFile(0, true);
    }

    /**
     * 根据偏移量查找MappedFile
     */
    public MappedFile findMappedFileByOffset(final long offset, final boolean returnFirstOnNotFound) {
        try {
            MappedFile mappedFileFirst = this.getFirstMappedFile();
            if (mappedFileFirst == null) {
                return null;
            }

            int index = (int) ((offset / this.mappedFileSize) - (mappedFileFirst.getFileFromOffset() / this.mappedFileSize));
            if (index < 0 || index >= this.mappedFiles.size()) {
                if (returnFirstOnNotFound) {
                    return mappedFileFirst;
                }
                return null;
            }

            return this.mappedFiles.get(index);
        } catch (Exception e) {
            log.error("findMappedFileByOffset exception", e);
        }
        return null;
    }

    /**
     * 获取第一个MappedFile
     */
    public MappedFile getFirstMappedFile() {
        if (this.mappedFiles.isEmpty()) {
            return null;
        }
        return this.mappedFiles.get(0);
    }

    /**
     * 获取所有MappedFile
     */
    public CopyOnWriteArrayList<MappedFile> getMappedFiles() {
        return mappedFiles;
    }

    /**
     * 删除最后一个MappedFile
     */
    public void deleteLastMappedFile() {
        if (!this.mappedFiles.isEmpty()) {
            MappedFile lastMappedFile = this.mappedFiles.remove(this.mappedFiles.size() - 1);
            lastMappedFile.destroy(1000);
            log.info("delete last mapped file {} success", lastMappedFile.getFileName());
        }
    }

    /**
     * 删除过期文件
     */
    public int deleteExpiredFileByTime(final long expiredTime, final int deleteFilesInterval,
                                       final long intervalForcibly, final boolean cleanImmediately) {
        Object[] mfs = this.mappedFiles.toArray();

        if (mfs == null || mfs.length == 0) {
            return 0;
        }

        int mfsLength = mfs.length - 1;
        int deleteCount = 0;
        List<MappedFile> files = new ArrayList<>();

        for (int i = 0; i < mfsLength; i++) {
            MappedFile mappedFile = (MappedFile) mfs[i];
            long liveMaxTimestamp = mappedFile.getLastModifiedTimestamp() + expiredTime;
            if (System.currentTimeMillis() >= liveMaxTimestamp || cleanImmediately) {
                if (mappedFile.destroy(intervalForcibly)) {
                    files.add(mappedFile);
                    deleteCount++;

                    if (files.size() >= deleteFilesInterval) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        this.mappedFiles.removeAll(files);

        return deleteCount;
    }

    /**
     * 计算数据偏移量
     */
    public long howMuchFallBehind() {
        if (this.mappedFiles.isEmpty()) {
            return 0;
        }

        long max = getMaxOffset();
        // flushedWhere 由 flush() 真实推进（老实现里没人更新它，只能退回 maxOffset，等于永远 0 落后）
        long flushed = this.flushedWhere;
        if (flushed == 0) {
            flushed = max;
        }

        return max - flushed;
    }

    /**
     * 获取最大偏移量（不创建文件：getMaxOffset 老实现走 getLastMappedFile()，会在读的时候顺手建文件）
     */
    public long getMaxOffset() {
        MappedFile mappedFile = peekLastMappedFile();
        if (mappedFile != null) {
            return mappedFile.getFileFromOffset() + mappedFile.getReadPosition();
        }
        return 0;
    }

    /**
     * 队列里"连续已落盘前缀"的末尾物理偏移。
     * <p>
     * 逐文件检查 {@code flushedPosition == wrotePosition}（该文件已写部分全部 force 过），
     * 一旦遇到没刷完的文件就停在这里，checkpoint 记录的正是这个连续前缀。
     */
    public long getFlushedOffset() {
        long flushed = 0L;
        for (MappedFile mappedFile : this.mappedFiles) {
            if (mappedFile.getFlushedPosition() != mappedFile.getWrotePosition()) {
                return flushed;
            }
            flushed = mappedFile.getFileFromOffset() + mappedFile.getWrotePosition();
        }
        return flushed;
    }

    /**
     * 已提交（进 page cache 之后由 commit 语义推进）的最大物理偏移。
     */
    public long getCommittedOffset() {
        MappedFile mappedFile = peekLastMappedFile();
        if (mappedFile != null) {
            return mappedFile.getFileFromOffset() + mappedFile.getCommittedPosition();
        }
        return this.committedWhere;
    }

    /**
     * 获取最小偏移量
     */
    public long getMinOffset() {
        if (!this.mappedFiles.isEmpty()) {
            return this.mappedFiles.get(0).getFileFromOffset();
        }
        return -1;
    }

    public long getFlushedWhere() {
        return flushedWhere;
    }

    public void setFlushedWhere(long flushedWhere) {
        this.flushedWhere = flushedWhere;
    }

    public long getCommittedWhere() {
        return committedWhere;
    }

    public void setCommittedWhere(long committedWhere) {
        this.committedWhere = committedWhere;
    }

    public String getStorePath() {
        return storePath;
    }

    public int getMappedFileSize() {
        return mappedFileSize;
    }

    /**
     * 获取MappedFile列表数量
     */
    public int getMappedFileCount() {
        return this.mappedFiles.size();
    }
}
