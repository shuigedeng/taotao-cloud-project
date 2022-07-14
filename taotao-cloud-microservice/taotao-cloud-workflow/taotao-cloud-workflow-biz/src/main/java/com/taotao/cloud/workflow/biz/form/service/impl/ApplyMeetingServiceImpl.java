package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import jnpf.base.service.BillRuleService;
import jnpf.base.util.FileManageUtil;
import jnpf.engine.service.FlowTaskService;
import jnpf.engine.util.ModelUtil;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ApplyMeetingEntity;
import jnpf.form.mapper.ApplyMeetingMapper;
import jnpf.form.model.applymeeting.ApplyMeetingForm;
import jnpf.form.service.ApplyMeetingService;
import jnpf.model.FileModel;
import jnpf.util.JsonUtil;
import jnpf.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会议申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Service
public class ApplyMeetingServiceImpl extends ServiceImpl<ApplyMeetingMapper, ApplyMeetingEntity> implements ApplyMeetingService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private FileManageUtil fileManageUtil;

    @Override
    public ApplyMeetingEntity getInfo(String id) {
        QueryWrapper<ApplyMeetingEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApplyMeetingEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, ApplyMeetingEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_ApplyMeetingNo");
            //添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            this.updateById(entity);
            //更新附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, ApplyMeetingEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_ApplyMeetingNo");
            //添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            this.updateById(entity);
            //更新附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null,candidateList);
    }

    @Override
    public void data(String id, String data) {
        ApplyMeetingForm applyBanquetForm = JsonUtil.getJsonToBean(data, ApplyMeetingForm.class);
        ApplyMeetingEntity entity = JsonUtil.getJsonToBean(applyBanquetForm, ApplyMeetingEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
