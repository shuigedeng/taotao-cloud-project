package com.taotao.cloud.prometheus.handler;


import com.taotao.cloud.prometheus.model.ExceptionNotice;
import com.taotao.cloud.prometheus.model.ExceptionStatistics;

public interface ExceptionNoticeStatisticsRepository {

	public ExceptionStatistics increaseOne(ExceptionNotice exceptionNotice);

	public void increaseShowOne(ExceptionStatistics exceptionStatistics);

	public void clear();
}
