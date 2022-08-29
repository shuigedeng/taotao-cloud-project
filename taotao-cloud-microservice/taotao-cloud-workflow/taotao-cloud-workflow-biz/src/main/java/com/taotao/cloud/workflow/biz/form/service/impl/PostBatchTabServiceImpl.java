package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ModelUtil;
import com.taotao.cloud.workflow.biz.form.entity.PostBatchTabEntity;
import com.taotao.cloud.workflow.biz.form.mapper.PostBatchTabMapper;
import com.taotao.cloud.workflow.biz.form.model.postbatchtab.PostBatchTabForm;
import com.taotao.cloud.workflow.biz.form.service.PostBatchTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发文呈批表
 */
@Service
public class PostBatchTabServiceImpl extends ServiceImpl<PostBatchTabMapper, PostBatchTabEntity> implements PostBatchTabService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private FileManageUtil fileManageUtil;

    @Override
    public PostBatchTabEntity getInfo(String id) {
        QueryWrapper<PostBatchTabEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PostBatchTabEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, PostBatchTabEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_PostBatchTabNo");
            //添加附件
            List<FileModel> data = JsonUtils.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            this.updateById(entity);
            //更新附件
            List<FileModel> data = JsonUtils.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, PostBatchTabEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_PostBatchTabNo");
            //添加附件
            List<FileModel> data = JsonUtils.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            this.updateById(entity);
            //更新附件
            List<FileModel> data = JsonUtils.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        PostBatchTabForm postBatchTabForm = JsonUtils.getJsonToBean(data, PostBatchTabForm.class);
        PostBatchTabEntity entity = JsonUtils.getJsonToBean(postBatchTabForm, PostBatchTabEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
