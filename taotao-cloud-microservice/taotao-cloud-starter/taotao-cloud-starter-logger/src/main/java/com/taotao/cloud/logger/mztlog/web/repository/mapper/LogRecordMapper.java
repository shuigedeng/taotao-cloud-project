package com.taotao.cloud.logger.mztlog.web.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.logger.mztlog.web.repository.po.LogRecordPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogRecordMapper extends BaseMapper<LogRecordPO> {
}
