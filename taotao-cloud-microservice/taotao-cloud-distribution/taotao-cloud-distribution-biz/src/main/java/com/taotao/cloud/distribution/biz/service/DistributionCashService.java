package com.taotao.cloud.distribution.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.distribution.api.vo.DistributionCashSearchVO;
import com.taotao.cloud.distribution.biz.entity.DistributionCash;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 分销佣金业务层
 */
public interface DistributionCashService extends IService<DistributionCash> {

    /**
     * 提交分销提现申请
     *
     * @param applyMoney 申请金额
     * @return 操作状态
     */
    Boolean cash(BigDecimal applyMoney);

    /**
     * 获取当前会员的分销提现分页列表
     *
     * @param page 分页
     * @return 申请提现分页
     */
    IPage<DistributionCash> getDistributionCash(PageVO page);

    /**
     * 获取分销员提现分页列表
     *
     * @param distributionCashSearchVO 搜索条件
     * @return 分销员提现分页列表
     */
    IPage<DistributionCash> getDistributionCash(
	    DistributionCashSearchVO distributionCashSearchVO);

    /**
     * 审核分销提现申请
     *
     * @param id     分销提现申请ID
     * @param result 处理结果
     * @return 分销提现申请
     */
    DistributionCash audit(@PathVariable String id, @RequestParam String result);

}
