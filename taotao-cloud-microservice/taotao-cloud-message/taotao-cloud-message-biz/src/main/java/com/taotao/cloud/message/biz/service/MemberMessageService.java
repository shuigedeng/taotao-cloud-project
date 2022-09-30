package com.taotao.cloud.message.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.message.api.vo.MemberMessageQueryVO;
import com.taotao.cloud.message.biz.entity.MemberMessage;

import java.util.List;

/**
 * 会员消息发送业务层
 */
public interface MemberMessageService extends IService<MemberMessage> {

	/**
	 * 会员消息查询接口
	 *
	 * @param memberMessageQueryVO 会员查询条件
	 * @param PageParam            分页条件
	 * @return 会员消息分页
	 */
	IPage<MemberMessage> getPage(MemberMessageQueryVO memberMessageQueryVO, PageParam PageParam);

	/**
	 * 修改会员消息状态
	 *
	 * @param status    状态
	 * @param messageId 消息id
	 * @return 操作状态
	 */
	Boolean editStatus(String status, String messageId);

	/**
	 * 删除消息
	 *
	 * @param messageId 消息id
	 * @return 操作状态
	 */
	Boolean deleteMessage(String messageId);

	/**
	 * 保存消息信息
	 *
	 * @param messages 消息
	 * @return
	 */
	Boolean save(List<MemberMessage> messages);


}
