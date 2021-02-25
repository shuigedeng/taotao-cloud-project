package com.taotao.cloud.uc.biz.service.impl;
import com.taotao.cloud.uc.biz.repository.SysJobRepository;
import com.taotao.cloud.uc.biz.service.ISysJobService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位表服务实现类
 *
 * @author dengtao
 * @date 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class SysJobServiceImpl  implements ISysJobService {
    private final SysJobRepository sysJobRepository;
}
