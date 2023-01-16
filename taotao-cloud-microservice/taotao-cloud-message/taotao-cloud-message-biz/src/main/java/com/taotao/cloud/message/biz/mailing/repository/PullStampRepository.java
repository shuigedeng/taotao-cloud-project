
package com.taotao.cloud.message.biz.mailing.repository;

import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.message.biz.mailing.entity.PullStamp;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;

/**
 *  PullStampRepository
 */
public interface PullStampRepository extends BaseRepository<PullStamp, String> {

	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<PullStamp> findByUserId(String userId);
}
