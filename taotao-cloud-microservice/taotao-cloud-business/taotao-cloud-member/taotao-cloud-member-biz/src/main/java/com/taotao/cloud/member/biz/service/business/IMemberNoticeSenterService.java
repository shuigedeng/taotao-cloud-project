package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.biz.model.entity.MemberNoticeSenter;

/**
 * 会员消息业务层
 */
public interface IMemberNoticeSenterService extends IService<MemberNoticeSenter> {

	/**
	 * 自定义保存方法
	 *
	 * @param memberNoticeSenter 会员消息
	 * @return 操作状态
	 */
	boolean customSave(MemberNoticeSenter memberNoticeSenter);

}
