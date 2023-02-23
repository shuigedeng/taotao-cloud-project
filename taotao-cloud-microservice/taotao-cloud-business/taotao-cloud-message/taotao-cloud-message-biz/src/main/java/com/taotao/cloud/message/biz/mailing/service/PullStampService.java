
package com.taotao.cloud.message.biz.mailing.service;

import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.message.biz.mailing.entity.PullStamp;
import com.taotao.cloud.message.biz.mailing.repository.PullStampRepository;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 *  MessagePullStampService
 */
@Service
public class PullStampService extends BaseLayeredService<PullStamp, String> {

	private final PullStampRepository pullStampRepository;

	public PullStampService(PullStampRepository pullStampRepository) {
		this.pullStampRepository = pullStampRepository;
	}

	@Override
	public BaseRepository<PullStamp, String> getRepository() {
		return pullStampRepository;
	}

	public PullStamp findByUserId(String userId) {
		return pullStampRepository.findByUserId(userId).orElse(null);
	}

	public PullStamp getPullStamp(String userId) {

		PullStamp stamp = findByUserId(userId);
		if (ObjectUtils.isEmpty(stamp)) {
			stamp = new PullStamp();
			stamp.setUserId(userId);
		}
		stamp.setLatestPullTime(new Date());

		return this.save(stamp);
	}
}
