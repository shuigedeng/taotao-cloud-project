package com.taotao.cloud.oss.common.model;


import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.constant.OssConstant;

/**
 * 断点续传参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:12
 */
public class SliceConfig {

	/**
	 * 分片大小,默认5MB
	 */
	private Long partSize = OssConstant.DEFAULT_PART_SIZE;

	/**
	 * 并发线程数,默认等于CPU的核数
	 */
	private Integer taskNum = OssConstant.DEFAULT_TASK_NUM;

	public void init() {
		if (this.getPartSize() <= 0) {
			LogUtils.warn("断点续传——分片大小必须大于0");
			this.setPartSize(OssConstant.DEFAULT_PART_SIZE);
		}
		if (this.getTaskNum() <= 0) {
			LogUtils.warn("断点续传——并发线程数必须大于0");
			this.setTaskNum(OssConstant.DEFAULT_TASK_NUM);
		}
	}

	public SliceConfig() {
	}

	public SliceConfig(Long partSize, Integer taskNum) {
		this.partSize = partSize;
		this.taskNum = taskNum;
	}

	public Long getPartSize() {
		return partSize;
	}

	public void setPartSize(Long partSize) {
		this.partSize = partSize;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}
}
