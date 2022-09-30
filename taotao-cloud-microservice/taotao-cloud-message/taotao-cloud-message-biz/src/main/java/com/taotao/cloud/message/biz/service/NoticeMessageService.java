package com.taotao.cloud.message.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.message.api.dto.NoticeMessageDTO;
import com.taotao.cloud.message.biz.entity.NoticeMessage;

/**
 * 通知类消息模板业务层
 */
public interface NoticeMessageService extends IService<NoticeMessage> {

	/**
	 * 多条件分页获取
	 *
	 * @param PageParam 分页数据
	 * @param type      类型
	 * @return
	 */
	IPage<NoticeMessage> getMessageTemplate(PageParam PageParam, String type);

	/**
	 * 根据模板编码获取消息模板
	 *
	 * @param noticeMessageDTO 站内信消息
	 */
	void noticeMessage(NoticeMessageDTO noticeMessageDTO);

}
