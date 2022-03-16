package com.taotao.cloud.sys.biz.entity.system.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sys.biz.entity.system.mapper.ServiceNoticeMapper;
import com.taotao.cloud.sys.biz.entity.system.service.ServiceNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务订阅消息业务层实现
 * @author Chopper
 * @since 2020/11/17 8:02 下午
 */
@Service
public class ServiceNoticeServiceImpl extends ServiceImpl<ServiceNoticeMapper, ServiceNotice> implements
	ServiceNoticeService {

}
