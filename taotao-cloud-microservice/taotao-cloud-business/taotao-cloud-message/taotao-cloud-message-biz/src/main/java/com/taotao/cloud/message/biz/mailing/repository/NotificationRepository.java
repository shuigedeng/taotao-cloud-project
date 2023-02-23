
package com.taotao.cloud.message.biz.mailing.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *  NotificationQueueRepository
 */
public interface NotificationRepository extends BaseRepository<Notification, String> {

	@Transactional(rollbackFor = Exception.class)
	@Modifying
	@Query("update Notification n set n.read = true where n.userId = :userId")
	int updateAllRead(@Param("userId") String userId);
}
