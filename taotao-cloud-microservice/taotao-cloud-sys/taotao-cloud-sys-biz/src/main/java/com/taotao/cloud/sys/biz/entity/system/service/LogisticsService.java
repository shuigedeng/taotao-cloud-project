package com.taotao.cloud.sys.biz.entity.system.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.cloud.sys.biz.entity.system.entity.dos.Logistics;
import com.taotao.cloud.sys.biz.entity.system.entity.vo.Traces;
import java.util.List;

/**
 * 物流公司业务层
 *
 * @author Chopper
 * @since 2020/11/17 8:02 下午
 */
public interface LogisticsService extends IService<Logistics> {

    /**
     * 查询物流信息
     *
     * @param logisticsId 物流公司ID
     * @param logisticsNo 单号
     * @return
     */
    Traces getLogistic(String logisticsId, String logisticsNo);

    /**
     * 获取已开启的物流公司列表
     *
     * @return 物流公司列表
     */
    List<Logistics> getOpenLogistics();
}
