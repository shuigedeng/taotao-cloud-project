package com.taotao.cloud.prometheus.pojos;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class ExceptionStatistics {

	/**
	 * 异常出现次数
	 */
	private AtomicLong showCount;

	/**
	 * 唯一id
	 */
	private String uid;

	/**
	 * 异常通知的时间
	 */
	private LocalDateTime noticeTime;

	/**
	 * 上一次通知时的次数
	 */
	private Long LastNoticedCount;

	/**
	 * 是否是第一次生成
	 */
	private boolean firstCreated = true;

	public ExceptionStatistics(String uid) {
		this.showCount = new AtomicLong(0);
		this.LastNoticedCount = 0L;
		this.uid = uid;
	}

	public ExceptionStatistics() {
	}

	public Long plusOne() {
		return showCount.incrementAndGet();
	}

	public void refreshShow() {
		this.LastNoticedCount = showCount.longValue();
		this.noticeTime = LocalDateTime.now();
	}

	/**
	 * @return the showCount
	 */
	public AtomicLong getShowCount() {
		return showCount;
	}

	/**
	 * @param showCount the showCount to set
	 */
	public void setShowCount(AtomicLong showCount) {
		this.showCount = showCount;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the noticeTime
	 */
	public LocalDateTime getNoticeTime() {
		return noticeTime;
	}

	/**
	 * @param noticeTime the noticeTime to set
	 */
	public void setNoticeTime(LocalDateTime noticeTime) {
		this.noticeTime = noticeTime;
	}

	/**
	 * @return the lastNoticedCount
	 */
	public Long getLastNoticedCount() {
		return LastNoticedCount;
	}

	/**
	 * @param lastNoticedCount the lastNoticedCount to set
	 */
	public void setLastNoticedCount(Long lastNoticedCount) {
		LastNoticedCount = lastNoticedCount;
	}

	/**
	 * @return the firstCreated
	 */
	public boolean isFirstCreated() {
		return firstCreated;
	}

	/**
	 * @param firstCreated the firstCreated to set
	 */
	public void setFirstCreated(boolean firstCreated) {
		this.firstCreated = firstCreated;
	}

	@Override
	public String toString() {
		return "ExceptionStatistics [showCount=" + showCount + ", uid=" + uid + ", noticeTime=" + noticeTime
				+ ", LastShowedCount=" + LastNoticedCount + ", firstCreated=" + firstCreated + "]";
	}

}
