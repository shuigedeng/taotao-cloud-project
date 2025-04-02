package com.taotao.cloud.job.server.persistence.mapper;

import com.taotao.cloud.job.server.persistence.domain.DistributedLock;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liushizhan
* @description 针对表【distributed_lock】的数据库操作Mapper
* @createDate 2024-10-19 14:58:59
* @Entity persistnece.domain.DistributedLock
*/
@Mapper
public interface DistributedLockMapper extends BaseMapper<DistributedLock> {

}




