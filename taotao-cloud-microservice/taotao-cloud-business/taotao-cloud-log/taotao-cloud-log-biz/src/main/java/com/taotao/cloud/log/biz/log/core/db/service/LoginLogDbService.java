package com.taotao.cloud.log.biz.log.core.db.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.data.mybatisplus.pagehelper.PageParam;
import com.taotao.cloud.log.biz.log.core.db.convert.LogConvert;
import com.taotao.cloud.log.biz.log.core.db.dao.LoginLogDbManager;
import com.taotao.cloud.log.biz.log.core.db.entity.LoginLogDb;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import com.taotao.cloud.log.biz.log.param.LoginLogParam;
import com.taotao.cloud.log.biz.log.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 登陆日志
 *
 * @author shuigedeng
 * @date 2021/8/12
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "log.store", value = "type", havingValue = "jdbc", matchIfMissing = true)
@RequiredArgsConstructor
public class LoginLogDbService implements LoginLogService {
	private final LoginLogDbManager loginLogManager;

	/**
	 * 添加
	 */
	@Override
	public void add(LoginLogParam loginLog) {
		loginLogManager.save(LogConvert.CONVERT.convert(loginLog));
	}

	/**
	 * 获取
	 */
	@Override
	public LoginLogDto findById(Long id) {
		return loginLogManager.findById(id).map(LoginLogDb::toDto).orElseThrow(RuntimeException::new);
	}

	/**
	 * 分页
	 */
	@Override
	public PageResult<LoginLogDto> page( LoginLogParam loginLogParam) {
		return PageResult.convertMybatisPage(loginLogManager.page(loginLogParam), LoginLogDto.class);
	}

	/**
	 * 删除
	 */
	@Override
	public void delete(Long id) {
		loginLogManager.deleteById(id);
	}
}
