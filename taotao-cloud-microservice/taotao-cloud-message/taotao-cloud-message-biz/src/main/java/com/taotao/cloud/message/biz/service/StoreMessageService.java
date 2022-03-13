package com.taotao.cloud.message.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.message.api.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.biz.entity.StoreMessage;
import java.util.List;

/**
 * 店铺接收消息业务层
 */
public interface StoreMessageService extends IService<StoreMessage> {


	/**
	 * 通过消息id删除
	 *
	 * @param messageId 消息ID
	 * @return 操作结果
	 */
	boolean deleteByMessageId(String messageId);

	/**
	 * 多条件分页获取
	 *
	 * @param storeMessageQueryVO 店铺消息查询VO
	 * @param pageVO              分页
	 * @return 店铺消息分页
	 */
	IPage<StoreMessage> getPage(StoreMessageQueryVO storeMessageQueryVO, PageVO pageVO);

	/**
	 * 保存店铺消息信息
	 *
	 * @param messages 消息
	 * @return
	 */
	boolean save(List<StoreMessage> messages);


	/**
	 * 修改店铺消息状态
	 *
	 * @param status 状态
	 * @param id     id
	 * @return
	 */
	boolean editStatus(String status, String id);

}
