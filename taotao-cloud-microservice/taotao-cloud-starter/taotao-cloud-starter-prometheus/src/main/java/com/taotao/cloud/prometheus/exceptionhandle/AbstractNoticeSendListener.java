package com.taotao.cloud.prometheus.exceptionhandle;

import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.ExceptionNotice;
import com.taotao.cloud.prometheus.model.ExceptionStatistics;
import com.taotao.cloud.prometheus.properties.ExceptionNoticeStrategyProperties;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationListener;


public abstract class AbstractNoticeSendListener implements ApplicationListener<ExceptionNoticeEvent> {

	private final ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties;

	private final ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository;

	private final List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents;

	/**
	 * @param exceptionNoticeStrategyProperties
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public AbstractNoticeSendListener(
		ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents) {
		this.exceptionNoticeStrategyProperties = exceptionNoticeStrategyProperties;
		this.exceptionNoticeStatisticsRepository = exceptionNoticeStatisticsRepository;
		this.noticeSendComponents = noticeSendComponents;
	}

	/**
	 * @return the exceptionNoticeFrequencyStrategy
	 */
	public ExceptionNoticeStrategyProperties getExceptionNoticeFrequencyStrategy() {
		return exceptionNoticeStrategyProperties;
	}

	/**
	 * @return the exceptionNoticeStatisticsRepository
	 */
	public ExceptionNoticeStatisticsRepository getExceptionNoticeStatisticsRepository() {
		return exceptionNoticeStatisticsRepository;
	}

	public void send(ExceptionNotice notice) {
		ExceptionStatistics statistics = exceptionNoticeStatisticsRepository.increaseOne(notice);
		if (stratergyCheck(statistics, exceptionNoticeStrategyProperties)) {
			notice.setShowCount(statistics.getShowCount().longValue());
			notice.setCreateTime(LocalDateTime.now());
			noticeSendComponents.forEach(x -> x.send(notice));
			exceptionNoticeStatisticsRepository.increaseShowOne(statistics);
		}
	}

	protected boolean stratergyCheck(ExceptionStatistics exceptionStatistics,
			ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties) {
		if (exceptionStatistics.isFirstCreated()) {
			exceptionStatistics.setFirstCreated(false);
			return true;
		}
		boolean flag = false;
		switch (exceptionNoticeStrategyProperties.getFrequencyType()) {
		case TIMEOUT:
			Duration dur = Duration.between(exceptionStatistics.getNoticeTime(), LocalDateTime.now());
			flag = exceptionNoticeStrategyProperties.getNoticeTimeInterval().compareTo(dur) < 0;
		case SHOWCOUNT:
			flag = exceptionStatistics.getShowCount().longValue() - exceptionStatistics.getLastNoticedCount()
					.longValue() > exceptionNoticeStrategyProperties.getNoticeShowCount().longValue();
		}
		return flag;
	}

}
