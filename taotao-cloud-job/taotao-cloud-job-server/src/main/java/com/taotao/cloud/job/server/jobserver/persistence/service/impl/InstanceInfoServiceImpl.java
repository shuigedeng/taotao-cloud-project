package com.taotao.cloud.job.server.jobserver.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.server.persistence.service.InstanceInfoService;
import com.taotao.cloud.server.persistence.mapper.InstanceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author shuigedeng
* @description 针对表【instance_info】的数据库操作Service实现
* @createDate 2024-10-20 20:12:43
*/
@Service
public class InstanceInfoServiceImpl extends ServiceImpl<InstanceInfoMapper, InstanceInfo>
    implements InstanceInfoService{

}




