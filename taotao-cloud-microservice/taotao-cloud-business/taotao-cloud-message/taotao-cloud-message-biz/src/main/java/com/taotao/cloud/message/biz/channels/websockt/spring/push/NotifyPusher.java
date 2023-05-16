package com.taotao.cloud.message.biz.channels.websockt.spring.push;


import com.taotao.cloud.message.biz.ballcat.notify.enums.NotifyChannelEnum;
import com.taotao.cloud.websocket.ballcat.notify.model.domain.NotifyInfo;

import java.util.List;

/**
 * 通知发布者
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
public interface NotifyPusher {

	/**
	 * 当前发布者对应的推送渠道
	 * @see NotifyChannelEnum
	 * @return 推送方式对应的标识
	 */
	Integer notifyChannel();

	/**
	 * 推送通知
	 * @param notifyInfo 通知信息
	 * @param userList 用户列表
	 */
	void push(NotifyInfo notifyInfo, List<SysUser> userList);

}
