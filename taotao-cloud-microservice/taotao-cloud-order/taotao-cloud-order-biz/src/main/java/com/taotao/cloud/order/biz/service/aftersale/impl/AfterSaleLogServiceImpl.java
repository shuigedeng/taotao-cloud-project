package com.taotao.cloud.order.biz.service.aftersale.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.order.biz.mapper.aftersale.AfterSaleLogMapper;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleLogService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单日志业务层实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AfterSaleLogServiceImpl extends
	ServiceImpl<AfterSaleLogMapper, AfterSaleLog> implements
	AfterSaleLogService {

	@Override
	public List<AfterSaleLog> getAfterSaleLog(String sn) {
		QueryWrapper queryWrapper = Wrappers.query();
		queryWrapper.eq("sn", sn);
		return this.list(queryWrapper);
	}
}
