package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;

import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowDelegateMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程委托
 *
 */
@Service
public class FlowDelegateServiceImpl extends ServiceImpl<FlowDelegateMapper, FlowDelegateEntity> implements FlowDelegateService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<FlowDelegateEntity> getList(Pagination pagination) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        String userId = userProvider.get().getUserId();
        QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowDelegateEntity::getCreatorUserId, userId);
        if (!StringUtils.isEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().and(
                    t -> t.like(FlowDelegateEntity::getFlowName, pagination.getKeyword())
                            .or().like(FlowDelegateEntity::getToUserName, pagination.getKeyword())
            );
        }
        //排序
        queryWrapper.lambda().orderByAsc(FlowDelegateEntity::getFSortCode).orderByDesc(FlowDelegateEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(FlowDelegateEntity::getLastModifyTime);
        }
        Page page = new Page(pagination.getCurrentPage(), pagination.getPageSize());
        IPage<FlowDelegateEntity> flowDelegateEntityPage = this.page(page, queryWrapper);
        return pagination.setData(flowDelegateEntityPage.getRecords(), page.getTotal());
    }


    @Override
    public List<FlowDelegateEntity> getList() {
        String userId = userProvider.get().getUserId();
        QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowDelegateEntity::getCreatorUserId, userId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public FlowDelegateEntity getInfo(String id) {
        QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowDelegateEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void delete(FlowDelegateEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public void create(FlowDelegateEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setFSortCode(RandomUtil.parses());
        entity.setCreatorUserId(userProvider.get().getUserId());
        this.save(entity);
    }

    @Override
    public List<FlowDelegateEntity> getUser(String userId) {
        return getUser(userId,null,null);
    }

    @Override
    public List<FlowDelegateEntity> getUser(String userId, String flowId, String creatorUserId) {
        Date thisTime = DateUtil.getNowDate();
        QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().le(FlowDelegateEntity::getStartTime, thisTime).ge(FlowDelegateEntity::getEndTime, thisTime);
        if(StringUtil.isNotEmpty(userId)){
            queryWrapper.lambda().eq(FlowDelegateEntity::getFTouserid, userId);
        }
        if(StringUtil.isNotEmpty(flowId)){
            queryWrapper.lambda().eq(FlowDelegateEntity::getFlowId, flowId);
        }
        if(StringUtil.isNotEmpty(creatorUserId)){
            queryWrapper.lambda().eq(FlowDelegateEntity::getCreatorUserId, creatorUserId);
        }
        return this.list(queryWrapper);
    }

    @Override
    public boolean update(String id, FlowDelegateEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(new Date());
        entity.setLastModifyUserId(userProvider.get().getUserId());
        return this.updateById(entity);
    }
}
