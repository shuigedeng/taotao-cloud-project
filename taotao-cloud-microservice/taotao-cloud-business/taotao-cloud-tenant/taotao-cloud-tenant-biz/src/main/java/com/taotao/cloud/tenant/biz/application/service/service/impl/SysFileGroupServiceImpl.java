package com.taotao.cloud.tenant.biz.application.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileGroup;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysFileGroupMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysFileGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 文件分组Service实现
 */
@Service
@RequiredArgsConstructor
public class SysFileGroupServiceImpl extends ServiceImpl<SysFileGroupMapper, SysFileGroup>
        implements ISysFileGroupService {

    @Override
    public List<SysFileGroup> listGroupWithFileCount() {
        return this.baseMapper.selectGroupWithFileCount();
    }

    @Override
    public Map<String, Object> getFileStatistics() {
        return this.baseMapper.selectFileStatistics();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createGroup(SysFileGroup group) {
        // 设置默认值
        if (group.getStatus() == null) {
            group.setStatus(1);
        }
        if (group.getSort() == null) {
            group.setSort(0);
        }
        if (group.getDeleted() == null) {
            group.setDeleted(0);
        }
        if (group.getParentId() == null) {
            group.setParentId(0L);
        }
        return this.save(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroup(SysFileGroup group) {
        return this.updateById(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroup(Long id) {
        // 逻辑删除分组
        return this.removeById(id);
    }
}
