package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.FinishedProductEntity;
import com.taotao.cloud.workflow.biz.form.entity.FinishedProductEntryEntity;
import com.taotao.cloud.workflow.biz.form.model.finishedproduct.FinishedProductEntryEntityInfoModel;
import com.taotao.cloud.workflow.biz.form.model.finishedproduct.FinishedProductForm;
import com.taotao.cloud.workflow.biz.form.model.finishedproduct.FinishedProductInfoVO;
import com.taotao.cloud.workflow.biz.form.service.FinishedProductService;

import java.util.List;
import javax.validation.Valid;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 成品入库单
 *
 */
@Tag(tags = "成品入库单", value = "FinishedProduct")
@RestController
@RequestMapping("/api/workflow/Form/FinishedProduct")
public class FinishedProductController {

    @Autowired
    private FinishedProductService finishedProductService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取成品入库单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取成品入库单信息")
    @GetMapping("/{id}")
    public Result<FinishedProductInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        FinishedProductInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtils.getJsonToBean(operator.getDraftData(), FinishedProductInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            FinishedProductEntity entity = finishedProductService.getInfo(id);
            List<FinishedProductEntryEntity> entityList = finishedProductService.getFinishedEntryList(id);
            vo = JsonUtils.getJsonToBean(entity, FinishedProductInfoVO.class);
            vo.setEntryList(JsonUtils.getJsonToList(entityList, FinishedProductEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建成品入库单
     *
     * @param finishedProductForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建成品入库单")
    @PostMapping
    public Result create(@RequestBody @Valid FinishedProductForm finishedProductForm) throws WorkFlowException {
        FinishedProductEntity finished = JsonUtils.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntity> finishedEntryList = JsonUtils.getJsonToList(finishedProductForm.getEntryList(), FinishedProductEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(finishedProductForm.getStatus())) {
            finishedProductService.save(finished.getId(), finished, finishedEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        finishedProductService.submit(finished.getId(), finished, finishedEntryList, finishedProductForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改成品入库单
     *
     * @param finishedProductForm 表单对象
     * @param id                  主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改成品入库单")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid FinishedProductForm finishedProductForm, @PathVariable("id") String id) throws WorkFlowException {
        FinishedProductEntity finished = JsonUtils.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntity> finishedEntryList = JsonUtils.getJsonToList(finishedProductForm.getEntryList(), FinishedProductEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(finishedProductForm.getStatus())) {
            finishedProductService.save(id, finished, finishedEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        finishedProductService.submit(id, finished, finishedEntryList, finishedProductForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
