package com.taotao.cloud.message.biz.service.impl;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.message.api.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.biz.entity.StoreMessage;
import com.taotao.cloud.message.biz.mapper.StoreMessageMapper;
import com.taotao.cloud.message.biz.service.StoreMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息发送业务层实现
 */
@Service
public class StoreMessageServiceImpl extends ServiceImpl<StoreMessageMapper, StoreMessage> implements
	StoreMessageService {

	@Override
	public Boolean deleteByMessageId(String messageId) {
		StoreMessage storeMessage = this.getById(messageId);
		if (storeMessage != null) {
			return this.removeById(messageId);
		}
		return false;

	}

	@Override
	public IPage<StoreMessage> getPage(StoreMessageQueryVO storeMessageQueryVO, PageParam PageParam) {

		QueryWrapper<StoreMessage> queryWrapper = new QueryWrapper<>();
		//消息id查询
		if (CharSequenceUtil.isNotEmpty(storeMessageQueryVO.getMessageId())) {
			queryWrapper.eq("message_id", storeMessageQueryVO.getMessageId());
		}
		//商家id
		if (CharSequenceUtil.isNotEmpty(storeMessageQueryVO.getStoreId())) {
			queryWrapper.eq("store_id", storeMessageQueryVO.getStoreId());
		}
		//状态查询
		if (storeMessageQueryVO.getStatus() != null) {
			queryWrapper.eq("status", storeMessageQueryVO.getStatus());
		}
		queryWrapper.orderByDesc("status");
		// return this.baseMapper.queryByParams(PageUtil.initPage(PageParam), queryWrapper);
		return null;
	}

	@Override
	public Boolean save(List<StoreMessage> messages) {
		return saveBatch(messages);
	}

	@Override
	public Boolean editStatus(String status, String id) {
		StoreMessage storeMessage = this.getById(id);
		if (storeMessage != null) {
			//校验权限
			// if (!storeMessage.getStoreId().equals(UserContext.getCurrentUser().getStoreId())) {
			//     throw new ResourceNotFoundException(ResultEnum.USER_AUTHORITY_ERROR.message());
			// }
			storeMessage.setStatus(status);
			return this.updateById(storeMessage);
		}
		return false;
	}
}
