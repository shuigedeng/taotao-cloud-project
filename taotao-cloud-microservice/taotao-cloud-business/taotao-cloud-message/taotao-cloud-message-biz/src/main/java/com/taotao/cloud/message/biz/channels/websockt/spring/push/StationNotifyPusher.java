package com.taotao.cloud.message.biz.channels.websockt.spring.push;

import com.taotao.cloud.message.biz.ballcat.notify.enums.NotifyChannelEnum;
import com.taotao.cloud.message.biz.ballcat.notify.event.StationNotifyPushEvent;
import com.taotao.cloud.websocket.ballcat.notify.model.domain.NotifyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息通知站内推送
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class StationNotifyPusher implements NotifyPusher {

	private final ApplicationEventPublisher publisher;

	/**
	 * 当前发布者对应的接收方式
	 *
	 * @return 推送方式
	 * @see NotifyChannelEnum
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannelEnum.STATION.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		// 发布事件，监听者进行实际的 websocket 推送
		publisher.publishEvent(new StationNotifyPushEvent(notifyInfo, userList));
	}

}
