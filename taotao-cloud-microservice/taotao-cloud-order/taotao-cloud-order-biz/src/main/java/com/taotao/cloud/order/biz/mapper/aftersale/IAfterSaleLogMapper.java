package com.taotao.cloud.order.biz.mapper.aftersale;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 售后日志数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:21
 */
@Mapper
public interface IAfterSaleLogMapper extends BaseMapper<AfterSaleLog> {

}
