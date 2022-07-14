package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ApplyDeliverGoodsEntity;
import jnpf.form.entity.ApplyDeliverGoodsEntryEntity;
import jnpf.form.model.applydelivergoods.ApplyDeliverGoodsEntryInfoModel;
import jnpf.form.model.applydelivergoods.ApplyDeliverGoodsForm;
import jnpf.form.model.applydelivergoods.ApplyDeliverGoodsInfoVO;
import jnpf.form.service.ApplyDeliverGoodsService;
import jnpf.util.JsonUtil;
import jnpf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发货申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "发货申请单", value = "ApplyDeliverGoods")
@RestController
@RequestMapping("/api/workflow/Form/ApplyDeliverGoods")
public class ApplyDeliverGoodsController {

    @Autowired
    private ApplyDeliverGoodsService applyDeliverGoodsService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取发货申请单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取发货申请单信息")
    @GetMapping("/{id}")
    public ActionResult<ApplyDeliverGoodsInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ApplyDeliverGoodsInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ApplyDeliverGoodsInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ApplyDeliverGoodsEntity entity = applyDeliverGoodsService.getInfo(id);
            List<ApplyDeliverGoodsEntryEntity> entityList = applyDeliverGoodsService.getDeliverEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, ApplyDeliverGoodsInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, ApplyDeliverGoodsEntryInfoModel.class));
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建发货申请单
     *
     * @param applyDeliverGoodsForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("新建发货申请单")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ApplyDeliverGoodsForm applyDeliverGoodsForm) throws WorkFlowException {
        ApplyDeliverGoodsEntity deliver = JsonUtil.getJsonToBean(applyDeliverGoodsForm, ApplyDeliverGoodsEntity.class);
        List<ApplyDeliverGoodsEntryEntity> deliverEntryList = JsonUtil.getJsonToList(applyDeliverGoodsForm.getEntryList(), ApplyDeliverGoodsEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyDeliverGoodsForm.getStatus())) {
            applyDeliverGoodsService.save(deliver.getId(), deliver, deliverEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        applyDeliverGoodsService.submit(deliver.getId(), deliver, deliverEntryList, applyDeliverGoodsForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改发货申请单
     *
     * @param applyDeliverGoodsForm 表单对象
     * @param id                    主键
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("修改发货申请单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ApplyDeliverGoodsForm applyDeliverGoodsForm, @PathVariable("id") String id) throws WorkFlowException {
        ApplyDeliverGoodsEntity deliver = JsonUtil.getJsonToBean(applyDeliverGoodsForm, ApplyDeliverGoodsEntity.class);
        List<ApplyDeliverGoodsEntryEntity> deliverEntryList = JsonUtil.getJsonToList(applyDeliverGoodsForm.getEntryList(), ApplyDeliverGoodsEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyDeliverGoodsForm.getStatus())) {
            applyDeliverGoodsService.save(id, deliver, deliverEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        applyDeliverGoodsService.submit(id, deliver, deliverEntryList, applyDeliverGoodsForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
