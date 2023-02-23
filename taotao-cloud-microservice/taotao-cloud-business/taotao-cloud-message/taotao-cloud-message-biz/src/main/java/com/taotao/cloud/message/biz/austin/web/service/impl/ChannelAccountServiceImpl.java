package com.taotao.cloud.message.biz.austin.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.message.biz.austin.common.constant.AustinConstant;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import com.taotao.cloud.message.biz.austin.web.service.ChannelAccountService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 */
@Service
public class ChannelAccountServiceImpl implements ChannelAccountService {

	@Autowired
	private ChannelAccountDao channelAccountDao;

	@Override
	public ChannelAccount save(ChannelAccount channelAccount) {
		if (Objects.isNull(channelAccount.getId())) {
			channelAccount.setCreated(Math.toIntExact(DateUtil.currentSeconds()));
			channelAccount.setIsDeleted(CommonConstant.FALSE);
		}
		channelAccount.setCreator(
			StrUtil.isBlank(channelAccount.getCreator()) ? AustinConstant.DEFAULT_CREATOR
				: channelAccount.getCreator());
		channelAccount.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
		return channelAccountDao.save(channelAccount);
	}

	@Override
	public List<ChannelAccount> queryByChannelType(Integer channelType, String creator) {
		return channelAccountDao.findAllByIsDeletedEqualsAndCreatorEqualsAndSendChannelEquals(
			CommonConstant.FALSE, creator, channelType);
	}

	@Override
	public List<ChannelAccount> list(String creator) {
		return channelAccountDao.findAllByCreatorEquals(creator);
	}

	@Override
	public void deleteByIds(List<Long> ids) {
		channelAccountDao.deleteAllById(ids);
	}
}
