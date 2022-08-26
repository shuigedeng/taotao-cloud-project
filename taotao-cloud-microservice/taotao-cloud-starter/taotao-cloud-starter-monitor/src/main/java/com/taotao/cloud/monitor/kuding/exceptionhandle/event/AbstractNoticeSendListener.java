package com.taotao.cloud.monitor.kuding.exceptionhandle.event;

import com.taotao.cloud.monitor.kuding.exceptionhandle.interfaces.ExceptionNoticeStatisticsRepository;
import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.pojos.ExceptionStatistics;
import com.taotao.cloud.monitor.kuding.pojos.notice.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.properties.exception.ExceptionNoticeFrequencyStrategyProperties;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.ApplicationListener;


public abstract class AbstractNoticeSendListener implements
	ApplicationListener<ExceptionNoticeEvent> {

	private final ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties;

	private final ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository;

	private final List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents;

	/**
	 * @param exceptionNoticeFrequencyStrategyProperties
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public AbstractNoticeSendListener(
		ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties,
		ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
		List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents) {
		this.exceptionNoticeFrequencyStrategyProperties = exceptionNoticeFrequencyStrategyProperties;
		this.exceptionNoticeStatisticsRepository = exceptionNoticeStatisticsRepository;
		this.noticeSendComponents = noticeSendComponents;
	}

	/**
	 * @return the exceptionNoticeFrequencyStrategy
	 */
	public ExceptionNoticeFrequencyStrategyProperties getExceptionNoticeFrequencyStrategy() {
		return exceptionNoticeFrequencyStrategyProperties;
	}

	/**
	 * @return the exceptionNoticeStatisticsRepository
	 */
	public ExceptionNoticeStatisticsRepository getExceptionNoticeStatisticsRepository() {
		return exceptionNoticeStatisticsRepository;
	}

	public void send(ExceptionNotice notice) {
		ExceptionStatistics statistics = exceptionNoticeStatisticsRepository.increaseOne(notice);
		if (strategyCheck(statistics, exceptionNoticeFrequencyStrategyProperties)) {
			notice.setShowCount(statistics.getShowCount().longValue());
			notice.setCreateTime(LocalDateTime.now());
			noticeSendComponents.forEach(x -> x.send(notice));
			exceptionNoticeStatisticsRepository.increaseShowOne(statistics);
		}
	}

	protected boolean strategyCheck(ExceptionStatistics exceptionStatistics,
		ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties) {
		if (exceptionStatistics.isFirstCreated()) {
			exceptionStatistics.setFirstCreated(false);
			return true;
		}
		boolean flag = false;
		switch (exceptionNoticeFrequencyStrategyProperties.getFrequencyType()) {
			case TIMEOUT:
				Duration dur = Duration.between(exceptionStatistics.getNoticeTime(),
					LocalDateTime.now());
				flag = exceptionNoticeFrequencyStrategyProperties.getNoticeTimeInterval().compareTo(dur) < 0;
			case SHOWCOUNT:
				flag = exceptionStatistics.getShowCount().longValue()
					- exceptionStatistics.getLastNoticedCount()
					.longValue() > exceptionNoticeFrequencyStrategyProperties.getNoticeShowCount()
					.longValue();
		}
		return flag;
	}

}
