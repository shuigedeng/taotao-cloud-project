package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.biz.repository.SysDeptRepository;
import com.taotao.cloud.uc.biz.service.ISysDeptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 后台部门表服务实现类
 *
 * @author dengtao
 * @date 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl implements ISysDeptService {
    private final SysDeptRepository sysDeptRepository;
}
