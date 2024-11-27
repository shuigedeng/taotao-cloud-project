package com.taotao.cloud.job.server.jobserver.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.server.persistence.domain.DistributedLock;

/**
* @author shuigedeng
* @description 针对表【distributed_lock】的数据库操作Mapper
* @createDate 2024-10-19 14:58:59
* @Entity persistnece.domain.DistributedLock
*/
@Mapper
public interface DistributedLockMapper extends BaseMapper<DistributedLock> {

}




