package com.taotao.cloud.member.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.biz.entity.MemberNoticeSenter;

/**
 * 会员消息业务层
 */
public interface MemberNoticeSenterService extends IService<MemberNoticeSenter> {

	/**
	 * 自定义保存方法
	 *
	 * @param memberNoticeSenter 会员消息
	 * @return 操作状态
	 */
	boolean customSave(MemberNoticeSenter memberNoticeSenter);

}
