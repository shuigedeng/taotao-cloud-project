package com.taotao.cloud.message.biz.channels.websockt.spring.handler.impl;

import com.taotao.cloud.message.biz.ballcat.notify.handler.AbstractNotifyInfoHandler;
import com.taotao.cloud.message.biz.ballcat.notify.model.domain.AnnouncementNotifyInfo;
import com.taotao.cloud.message.biz.ballcat.notify.model.entity.UserAnnouncement;
import com.taotao.cloud.message.biz.ballcat.notify.service.UserAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 公告通知消息处理器
 *
 * @author huyuanzhi
 */
@Component
@RequiredArgsConstructor
public class AnnouncementNotifyInfoHandler
	extends AbstractNotifyInfoHandler<AnnouncementNotifyInfo, AnnouncementPushMessage> {

	private final UserAnnouncementService userAnnouncementService;

	@Override
	protected void persistMessage(List<SysUser> userList, AnnouncementNotifyInfo announcementNotifyInfo) {
		List<UserAnnouncement> userAnnouncements = new ArrayList<>(userList.size());
		// 向指定用户推送
		for (SysUser sysUser : userList) {
			Integer userId = sysUser.getUserId();
			UserAnnouncement userAnnouncement = userAnnouncementService.prodUserAnnouncement(userId,
				announcementNotifyInfo.getId());
			userAnnouncements.add(userAnnouncement);
		}
		userAnnouncementService.saveBatch(userAnnouncements);
	}

	@Override
	protected AnnouncementPushMessage createMessage(AnnouncementNotifyInfo announcementNotifyInfo) {
		AnnouncementPushMessage message = new AnnouncementPushMessage();
		message.setId(announcementNotifyInfo.getId());
		message.setTitle(announcementNotifyInfo.getTitle());
		message.setContent(announcementNotifyInfo.getContent());
		message.setImmortal(announcementNotifyInfo.getImmortal());
		message.setDeadline(announcementNotifyInfo.getDeadline());
		return message;
	}

}
