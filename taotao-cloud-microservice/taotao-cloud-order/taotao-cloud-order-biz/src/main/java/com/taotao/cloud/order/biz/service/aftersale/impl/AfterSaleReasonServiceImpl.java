package com.taotao.cloud.order.biz.service.aftersale.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.api.query.aftersale.AfterSaleReasonPageQuery;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.order.biz.mapper.aftersale.IAfterSaleReasonMapper;
import com.taotao.cloud.order.biz.service.aftersale.IAfterSaleReasonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 售后原因业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:27
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AfterSaleReasonServiceImpl extends
	ServiceImpl<IAfterSaleReasonMapper, AfterSaleReason> implements IAfterSaleReasonService {

	@Override
	public List<AfterSaleReason> afterSaleReasonList(String serviceType) {
		LambdaQueryWrapper<AfterSaleReason> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(AfterSaleReason::getServiceType, serviceType);
		return this.list(lambdaQueryWrapper);
	}

	@Override
	public Boolean editAfterSaleReason(AfterSaleReason afterSaleReason) {
		LambdaUpdateWrapper<AfterSaleReason> lambdaQueryWrapper = Wrappers.lambdaUpdate();
		lambdaQueryWrapper.eq(AfterSaleReason::getId, afterSaleReason.getId());
		lambdaQueryWrapper.set(AfterSaleReason::getReason, afterSaleReason.getReason());
		lambdaQueryWrapper.set(AfterSaleReason::getServiceType, afterSaleReason.getServiceType());
		this.update(lambdaQueryWrapper);
		return true;
	}

    @Override
    public IPage<AfterSaleReason> getByPage(AfterSaleReasonPageQuery afterSaleReasonPageQuery) {
		LambdaQueryWrapper<AfterSaleReason> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(AfterSaleReason::getServiceType, afterSaleReasonPageQuery.getServiceType());
        return this.page(afterSaleReasonPageQuery.buildMpPage(), queryWrapper);
    }
}
