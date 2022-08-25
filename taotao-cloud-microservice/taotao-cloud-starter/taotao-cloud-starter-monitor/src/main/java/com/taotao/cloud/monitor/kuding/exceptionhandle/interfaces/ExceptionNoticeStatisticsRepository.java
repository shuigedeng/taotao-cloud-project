package com.taotao.cloud.monitor.kuding.exceptionhandle.interfaces;


import com.taotao.cloud.monitor.kuding.pojos.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.pojos.ExceptionStatistics;

public interface ExceptionNoticeStatisticsRepository {

	public ExceptionStatistics increaseOne(ExceptionNotice exceptionNotice);

	public void increaseShowOne(ExceptionStatistics exceptionStatistics);

	public void clear();
}
