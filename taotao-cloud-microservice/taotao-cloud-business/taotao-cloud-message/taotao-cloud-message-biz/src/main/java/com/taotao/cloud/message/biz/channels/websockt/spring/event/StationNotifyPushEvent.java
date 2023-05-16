package com.taotao.cloud.message.biz.channels.websockt.spring.event;

import com.taotao.cloud.message.biz.ballcat.notify.model.domain.NotifyInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class StationNotifyPushEvent {

	/**
	 * 通知信息
	 */
	private final NotifyInfo notifyInfo;

	/**
	 * 推送用户列表
	 */
	private final List<SysUser> userList;

}
