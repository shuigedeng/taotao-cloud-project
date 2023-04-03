package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.data.mybatisplus.utils.BaseManager;
import com.taotao.cloud.log.biz.log.core.db.entity.OperateLogDb;
import com.taotao.cloud.log.biz.log.param.OperateLogParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @date 2021/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogDbManager extends BaseManager<OperateLogDbMapper, OperateLogDb> {

	public IPage<OperateLogDb> page(OperateLogParam operateLogParam) {
		return lambdaQuery()
			.like(StrUtil.isNotBlank(operateLogParam.getUsername()), OperateLogDb::getUsername, operateLogParam.getUsername())
			.like(StrUtil.isNotBlank(operateLogParam.getTitle()), OperateLogDb::getTitle, operateLogParam.getTitle())
			.eq(Objects.nonNull(operateLogParam.getBusinessType()), OperateLogDb::getBusinessType, operateLogParam.getBusinessType())
			.orderByDesc(OperateLogDb::getOperateTime)
			.page(operateLogParam.buildMpPage());
	}
}
