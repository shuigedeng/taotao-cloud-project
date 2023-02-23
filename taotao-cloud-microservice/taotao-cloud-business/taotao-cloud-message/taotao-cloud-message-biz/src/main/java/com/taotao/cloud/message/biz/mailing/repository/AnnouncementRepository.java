
package com.taotao.cloud.message.biz.mailing.repository;

import jakarta.persistence.QueryHint;
import java.util.Date;
import java.util.List;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;

/**
 *  SystemAnnouncementRepository
 */
public interface AnnouncementRepository extends BaseRepository<Announcement, String> {

	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	List<Announcement> findAllByCreateTimeAfter(Date stamp);
}
