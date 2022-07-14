package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.workflow.biz.form.entity.MaterialEntryEntity;
import com.taotao.cloud.workflow.biz.form.mapper.MaterialEntryMapper;
import com.taotao.cloud.workflow.biz.form.service.MaterialEntryService;
import org.springframework.stereotype.Service;

/**
 * 领料单明细
 */
@Service
public class MaterialEntryServiceImpl extends ServiceImpl<MaterialEntryMapper, MaterialEntryEntity> implements MaterialEntryService {

}
