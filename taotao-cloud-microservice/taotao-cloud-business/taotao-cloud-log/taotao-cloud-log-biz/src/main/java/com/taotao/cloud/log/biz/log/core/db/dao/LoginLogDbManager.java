package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.data.mybatisplus.pagehelper.PageParam;
import com.taotao.cloud.data.mybatisplus.utils.BaseManager;
import com.taotao.cloud.log.biz.log.core.db.entity.LoginLogDb;
import com.taotao.cloud.log.biz.log.param.LoginLogParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 登录日志
 *
 * @author shuigedeng
 * @date 2021/8/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginLogDbManager extends BaseManager<LoginLogDbMapper, LoginLogDb> {

	public IPage<LoginLogDb> page(LoginLogParam loginLogParam) {
		return lambdaQuery()
			.orderByDesc(LoginLogDb::getId)
			.like(StrUtil.isNotBlank(loginLogParam.getAccount()), LoginLogDb::getAccount, loginLogParam.getAccount())
			.like(StrUtil.isNotBlank(loginLogParam.getClient()), LoginLogDb::getClient, loginLogParam.getClient())
			.like(StrUtil.isNotBlank(loginLogParam.getLoginType()), LoginLogDb::getLoginType, loginLogParam.getLoginType())
			.page(loginLogParam.buildMpPage());
	}
}
