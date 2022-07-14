package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.base.util.RegexUtils;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ApplyBanquetEntity;
import jnpf.form.model.applybanquet.ApplyBanquetForm;
import jnpf.form.model.applybanquet.ApplyBanquetInfoVO;
import jnpf.form.service.ApplyBanquetService;
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
 * 宴请申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "宴请申请", value = "ApplyBanquet")
@RestController
@RequestMapping("/api/workflow/Form/ApplyBanquet")
public class ApplyBanquetController {

    @Autowired
    private ApplyBanquetService applyBanquetService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取宴请申请信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取宴请申请信息")
    @GetMapping("/{id}")
    public ActionResult<ApplyBanquetInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ApplyBanquetInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ApplyBanquetInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ApplyBanquetEntity entity = applyBanquetService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ApplyBanquetInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建宴请申请
     *
     * @param applyBanquetForm 表单对象
     * @return
     */
    @ApiOperation("新建宴请申请")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ApplyBanquetForm applyBanquetForm) throws WorkFlowException {
        if (applyBanquetForm.getBanquetNum() != null && StringUtil.isNotEmpty(applyBanquetForm.getBanquetNum()) && !RegexUtils.checkDigit2(applyBanquetForm.getBanquetNum())) {
            return ActionResult.fail("宴请人数必须大于0");
        }
        if (applyBanquetForm.getTotal() != null && StringUtil.isNotEmpty(applyBanquetForm.getTotal()) && !RegexUtils.checkDigit2(applyBanquetForm.getTotal())) {
            return ActionResult.fail("人员总数必须大于0");
        }
        if (applyBanquetForm.getExpectedCost() != null && !RegexUtils.checkDecimals2(String.valueOf(applyBanquetForm.getExpectedCost()))) {
            return ActionResult.fail("预计费用必须大于0，最多只能有两位小数");
        }
        ApplyBanquetEntity entity = JsonUtil.getJsonToBean(applyBanquetForm, ApplyBanquetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyBanquetForm.getStatus())) {
            applyBanquetService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        applyBanquetService.submit(entity.getId(), entity, applyBanquetForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改宴请申请
     *
     * @param applyBanquetForm 表单对象
     * @param id               主键
     * @return
     */
    @ApiOperation("修改宴请申请")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ApplyBanquetForm applyBanquetForm, @PathVariable("id") String id) throws WorkFlowException {
        if (applyBanquetForm.getBanquetNum() != null && StringUtil.isNotEmpty(applyBanquetForm.getBanquetNum()) && !RegexUtils.checkDigit2(applyBanquetForm.getBanquetNum())) {
            return ActionResult.fail("宴请人数必须大于0");
        }
        if (applyBanquetForm.getTotal() != null && StringUtil.isNotEmpty(applyBanquetForm.getTotal()) && !RegexUtils.checkDigit2(applyBanquetForm.getTotal())) {
            return ActionResult.fail("人员总数必须大于0");
        }
        if (applyBanquetForm.getExpectedCost() != null && !RegexUtils.checkDecimals2(String.valueOf(applyBanquetForm.getExpectedCost()))) {
            return ActionResult.fail("预计费用必须大于0，最多只能有两位小数");
        }
        ApplyBanquetEntity entity = JsonUtil.getJsonToBean(applyBanquetForm, ApplyBanquetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyBanquetForm.getStatus())) {
            applyBanquetService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        applyBanquetService.submit(id, entity, applyBanquetForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
