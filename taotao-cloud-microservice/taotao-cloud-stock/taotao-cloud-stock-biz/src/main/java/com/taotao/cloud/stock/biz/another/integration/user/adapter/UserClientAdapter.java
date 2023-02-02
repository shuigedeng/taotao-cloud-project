package com.taotao.cloud.stock.biz.another.integration.user.adapter;

import com.taotao.cloud.ddd.biz.integration.user.vo.UserAddressVO;
import com.taotao.cloud.ddd.biz.integration.user.vo.UserBaseInfoVO;
import com.taotao.cloud.ddd.biz.integration.user.vo.UserContactVO;

public class UserClientAdapter {

	public UserBaseInfoVO convert(UserInfoClientDTO userInfo) {
		// 基础信息
		UserBaseInfoVO userBaseInfo = new UserBaseInfoVO();
		// 联系方式
		UserContactVO contactVO = new UserContactVO();
		contactVO.setMobile(userInfo.getMobile());
		userBaseInfo.setContactInfo(contactVO);
		// 地址信息
		UserAddressVO addressVO = new UserAddressVO();
		addressVO.setCityCode(userInfo.getCityCode());
		addressVO.setAddressDetail(userInfo.getAddressDetail());
		userBaseInfo.setAddressInfo(addressVO);
		return userBaseInfo;
	}
}
