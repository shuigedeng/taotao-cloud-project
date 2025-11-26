package com.taotao.cloud.sys.biz.config.i18n.listener;

import com.taotao.cloud.sys.biz.model.convert.I18nDataConverter;
import com.taotao.cloud.sys.biz.model.entity.i18n.I18nData;
import com.taotao.cloud.sys.biz.service.business.I18nDataService;
import com.taotao.boot.web.i18n.I18nMessage;
import com.taotao.boot.web.i18n.I18nMessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * I18nMessage 创建事件的监听者
 */
@Component
@RequiredArgsConstructor
public class I18nMessageCreateListener {

	private final I18nDataService i18nDataService;

	/**
	 * 监听 I18nMessageCreateEvent 事件，保存对应的 I18nMessage
	 *
	 * @param event the event
	 */
	@EventListener(I18nMessageCreateEvent.class)
	public void saveOnI18nMessageCreateEvent(I18nMessageCreateEvent event) {
		List<I18nMessage> i18nMessages = event.getI18nMessages();
		List<I18nData> list = i18nMessages.stream()
			.map(I18nDataConverter.INSTANCE::messageToPo)
			.toList();
		i18nDataService.saveBatch(list);
	}

}
