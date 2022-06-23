package com.taotao.cloud.order.biz.service.aftersale.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.order.biz.mapper.aftersale.IAfterSaleLogMapper;
import com.taotao.cloud.order.biz.service.aftersale.IAfterSaleLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单日志业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:24
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AfterSaleLogServiceImpl extends
	ServiceImpl<IAfterSaleLogMapper, AfterSaleLog> implements IAfterSaleLogService {

	@Override
	public List<AfterSaleLog> getAfterSaleLog(String sn) {
		LambdaQueryWrapper<AfterSaleLog> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(AfterSaleLog::getSn, sn);
		return this.list(queryWrapper);
	}
}




