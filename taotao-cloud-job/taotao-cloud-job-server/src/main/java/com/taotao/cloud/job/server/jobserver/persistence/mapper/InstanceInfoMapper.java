package com.taotao.cloud.job.server.jobserver.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.taotao.cloud.server.persistence.domain.InstanceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author shuigedeng
* @description 针对表【instance_info】的数据库操作Mapper
* @createDate 2024-10-20 20:12:43
* @Entity com.taotao.cloud.server.persistence.domain.InstanceInfo
*/
@Mapper
public interface InstanceInfoMapper extends BaseMapper<InstanceInfo> {

}




