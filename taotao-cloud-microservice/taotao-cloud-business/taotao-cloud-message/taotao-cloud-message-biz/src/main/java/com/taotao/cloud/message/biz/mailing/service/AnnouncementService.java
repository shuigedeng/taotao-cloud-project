
package com.taotao.cloud.message.biz.mailing.service;

import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.message.biz.mailing.entity.Announcement;
import com.taotao.cloud.message.biz.mailing.repository.AnnouncementRepository;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *  SystemAnnouncementService
 */
@Service
public class AnnouncementService extends BaseLayeredService<Announcement, String> {

	private static final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

	private final AnnouncementRepository announcementRepository;

	public AnnouncementService(AnnouncementRepository announcementRepository) {
		this.announcementRepository = announcementRepository;
	}

	@Override
	public BaseRepository<Announcement, String> getRepository() {
		return announcementRepository;
	}

	public List<Announcement> pullAnnouncements(Date stamp) {
		List<Announcement> announcements = announcementRepository.findAllByCreateTimeAfter(stamp);
		log.debug("[Websocket] |- Announcement Service pullAnnouncements.");
		return announcements;
	}
}
