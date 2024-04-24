package com.taotao.cloud.order.application.statemachine.cola.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.domain.AuditDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @date 2023/7/12 16:42
 */
@Mapper
public interface AuditMapper extends BaseMapper<AuditDO> {

}
