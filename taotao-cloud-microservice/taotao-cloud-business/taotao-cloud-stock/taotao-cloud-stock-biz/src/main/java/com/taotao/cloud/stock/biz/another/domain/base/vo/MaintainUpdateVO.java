package com.taotao.cloud.stock.biz.another.domain.base.vo;

import com.taotao.cloud.ddd.biz.util.validate.BizValidator;
import com.taotao.cloud.stock.biz.another.client.base.error.BizException;

// 修改人值对象
public class MaintainUpdateVO implements BizValidator {

	// 修改人
	private String updator;
	// 修改时间
	private Date updateTime;

	@Override
	public void validate() {
		if (null == updator) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
		if (null == updateTime) {
			throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
		}
	}
}
