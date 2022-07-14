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
import jnpf.form.entity.FinishedProductEntity;
import jnpf.form.entity.FinishedProductEntryEntity;
import jnpf.form.model.finishedproduct.FinishedProductEntryEntityInfoModel;
import jnpf.form.model.finishedproduct.FinishedProductForm;
import jnpf.form.model.finishedproduct.FinishedProductInfoVO;
import jnpf.form.service.FinishedProductService;
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
 * 成品入库单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "成品入库单", value = "FinishedProduct")
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
    @ApiOperation("获取成品入库单信息")
    @GetMapping("/{id}")
    public ActionResult<FinishedProductInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        FinishedProductInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), FinishedProductInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            FinishedProductEntity entity = finishedProductService.getInfo(id);
            List<FinishedProductEntryEntity> entityList = finishedProductService.getFinishedEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, FinishedProductInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, FinishedProductEntryEntityInfoModel.class));
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建成品入库单
     *
     * @param finishedProductForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("新建成品入库单")
    @PostMapping
    public ActionResult create(@RequestBody @Valid FinishedProductForm finishedProductForm) throws WorkFlowException {
        FinishedProductEntity finished = JsonUtil.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntity> finishedEntryList = JsonUtil.getJsonToList(finishedProductForm.getEntryList(), FinishedProductEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(finishedProductForm.getStatus())) {
            finishedProductService.save(finished.getId(), finished, finishedEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        finishedProductService.submit(finished.getId(), finished, finishedEntryList, finishedProductForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改成品入库单
     *
     * @param finishedProductForm 表单对象
     * @param id                  主键
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("修改成品入库单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid FinishedProductForm finishedProductForm, @PathVariable("id") String id) throws WorkFlowException {
        FinishedProductEntity finished = JsonUtil.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntity> finishedEntryList = JsonUtil.getJsonToList(finishedProductForm.getEntryList(), FinishedProductEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(finishedProductForm.getStatus())) {
            finishedProductService.save(id, finished, finishedEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        finishedProductService.submit(id, finished, finishedEntryList, finishedProductForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
