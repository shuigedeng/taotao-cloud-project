package com.taotao.cloud.monitor.kuding.exceptionhandle.components;

import com.taotao.cloud.monitor.kuding.pojos.notice.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.pojos.ExceptionStatistics;
import com.taotao.cloud.monitor.kuding.exceptionhandle.interfaces.ExceptionNoticeStatisticsRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class InMemeryExceptionStatisticsRepository implements ExceptionNoticeStatisticsRepository {

	private final Map<String, ExceptionStatistics> map = Collections.synchronizedMap(new HashMap<>());

	@Override
	public ExceptionStatistics increaseOne(ExceptionNotice exceptionNotice) {
		ExceptionStatistics exceptionStatistics = map.getOrDefault(exceptionNotice.getUid(),
				new ExceptionStatistics(exceptionNotice.getUid()));
		if (exceptionStatistics.isFirstCreated()) {
			synchronized (exceptionStatistics) {
				map.merge(exceptionStatistics.getUid(), exceptionStatistics, (x, y) -> {
					if (x == null) {
						return y;
					} else {
						x.setFirstCreated(false);
						return x;
					}
				});
			}
		}
		exceptionStatistics.plusOne();
		return exceptionStatistics;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void increaseShowOne(ExceptionStatistics exceptionStatistics) {
		exceptionStatistics.refreshShow();
	}

}
