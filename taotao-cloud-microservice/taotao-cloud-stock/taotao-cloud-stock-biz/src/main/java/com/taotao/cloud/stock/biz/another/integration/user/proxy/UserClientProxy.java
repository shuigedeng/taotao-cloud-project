package com.taotao.cloud.stock.biz.another.integration.user.proxy;

import com.taotao.cloud.ddd.biz.integration.user.adapter.UserClientAdapter;
import com.taotao.cloud.stock.biz.another.integration.user.vo.UserBaseInfoVO;

public class UserClientProxy {

	@Resource
	private UserClientService userClientService;
	@Resource
	private UserClientAdapter userIntegrationAdapter;

	// 查询用户
	public UserBaseInfoVO getUserInfo(String userId) {
		UserInfoClientDTO user = userClientService.getUserInfo(userId);
		UserBaseInfoVO result = userIntegrationAdapter.convert(user);
		return result;
	}
}

