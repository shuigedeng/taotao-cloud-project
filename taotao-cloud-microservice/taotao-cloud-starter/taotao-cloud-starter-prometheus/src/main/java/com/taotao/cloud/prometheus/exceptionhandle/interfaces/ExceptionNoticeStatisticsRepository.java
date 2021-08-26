package com.taotao.cloud.prometheus.exceptionhandle.interfaces;


import com.taotao.cloud.prometheus.pojos.ExceptionNotice;
import com.taotao.cloud.prometheus.pojos.ExceptionStatistics;

public interface ExceptionNoticeStatisticsRepository {

	public ExceptionStatistics increaseOne(ExceptionNotice exceptionNotice);

	public void increaseShowOne(ExceptionStatistics exceptionStatistics);

	public void clear();
}
