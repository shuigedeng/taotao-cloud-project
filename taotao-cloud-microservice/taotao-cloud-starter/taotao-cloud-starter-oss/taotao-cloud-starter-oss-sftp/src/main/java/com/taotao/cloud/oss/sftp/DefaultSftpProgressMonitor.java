/**
 * $Id: DefaultSftpProgressMonitor.java,v 1.0 2022/2/12 5:11 PM chenmin Exp $
 */
package com.taotao.cloud.oss.sftp;

import com.jcraft.jsch.SftpProgressMonitor;
import com.taotao.cloud.common.utils.log.LogUtils;

import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 默认sftp进度监控
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:54
 */
public class DefaultSftpProgressMonitor implements SftpProgressMonitor, Runnable {

    private long maxFileSize = 0L;
    private long startTime = 0L;
    private long upLoaded = 0L;
    private boolean isScheduled = false;

    private ScheduledExecutorService executorService;

    public DefaultSftpProgressMonitor(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

	public DefaultSftpProgressMonitor(long maxFileSize, long startTime, long upLoaded,
		boolean isScheduled, ScheduledExecutorService executorService) {
		this.maxFileSize = maxFileSize;
		this.startTime = startTime;
		this.upLoaded = upLoaded;
		this.isScheduled = isScheduled;
		this.executorService = executorService;
	}

	@Override
    public void run() {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        String value = format.format((upLoaded / (double) maxFileSize));
        if (LogUtils.isDebugEnabled()) {
	        LogUtils.debug("已传输:{}KB,传输进度:{}", upLoaded/1024, value);
        }
        if (upLoaded == maxFileSize) {
            destoryThread();
            long endTime = System.currentTimeMillis();
            if (LogUtils.isDebugEnabled()) {
	            LogUtils.debug("传输完成!用时:{}s", (endTime - startTime)/1000);
            }
        }
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        if (LogUtils.isDebugEnabled()) {
	        LogUtils.debug("开始传输文件:{},文件总大小为:{}KB", src, maxFileSize/1024);
        }
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean count(long count) {
        if (!isScheduled) {
            createThread();
        }
        upLoaded += count;
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void end() {

    }

    /**
     * 创建线程,定时输出上传进度
     */
    public void createThread() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this, 1, 2, TimeUnit.SECONDS);
        isScheduled = true;
    }

    public void destoryThread() {
        boolean isShutdown = executorService.isShutdown();
        if (!isShutdown) {
            executorService.shutdown();
        }
    }

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getUpLoaded() {
		return upLoaded;
	}

	public void setUpLoaded(long upLoaded) {
		this.upLoaded = upLoaded;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean scheduled) {
		isScheduled = scheduled;
	}

	public ScheduledExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ScheduledExecutorService executorService) {
		this.executorService = executorService;
	}
}
