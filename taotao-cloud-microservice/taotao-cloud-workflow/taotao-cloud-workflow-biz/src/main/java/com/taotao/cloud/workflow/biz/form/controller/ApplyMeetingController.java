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
import jnpf.form.entity.ApplyMeetingEntity;
import jnpf.form.model.applymeeting.ApplyMeetingForm;
import jnpf.form.model.applymeeting.ApplyMeetingInfoVO;
import jnpf.form.service.ApplyMeetingService;
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
 * 会议申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "会议申请", value = "ApplyMeeting")
@RestController
@RequestMapping("/api/workflow/Form/ApplyMeeting")
public class ApplyMeetingController {

    @Autowired
    private ApplyMeetingService applyMeetingService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取会议申请信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取会议申请信息")
    @GetMapping("/{id}")
    public ActionResult<ApplyMeetingInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ApplyMeetingInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ApplyMeetingInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ApplyMeetingEntity entity = applyMeetingService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ApplyMeetingInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建会议申请
     *
     * @param applyMeetingForm 表单对象
     * @return
     */
    @ApiOperation("新建会议申请")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ApplyMeetingForm applyMeetingForm) throws WorkFlowException {
        if (applyMeetingForm.getStartDate() > applyMeetingForm.getEndDate()) {
            return ActionResult.fail("结束时间不能小于开始时间");
        }
        if (applyMeetingForm.getEstimatePeople() != null && StringUtil.isNotEmpty(applyMeetingForm.getEstimatePeople()) && !RegexUtils.checkDigit2(applyMeetingForm.getEstimatePeople())) {
            return ActionResult.fail("预计人数只能输入正整数");
        }
        if (applyMeetingForm.getEstimatedAmount() != null && !RegexUtils.checkDecimals2(String.valueOf(applyMeetingForm.getEstimatedAmount()))) {
            return ActionResult.fail("预计金额必须大于0，最多精确小数点后两位");
        }
        ApplyMeetingEntity entity = JsonUtil.getJsonToBean(applyMeetingForm, ApplyMeetingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyMeetingForm.getStatus())) {
            applyMeetingService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        applyMeetingService.submit(entity.getId(), entity, applyMeetingForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改会议申请
     *
     * @param applyMeetingForm 表单对象
     * @param id               主键
     * @return
     */
    @ApiOperation("修改会议申请")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ApplyMeetingForm applyMeetingForm, @PathVariable("id") String id) throws WorkFlowException {
        if (applyMeetingForm.getStartDate() > applyMeetingForm.getEndDate()) {
            return ActionResult.fail("结束时间不能小于开始时间");
        }
        if (applyMeetingForm.getEstimatePeople() != null && StringUtil.isNotEmpty(applyMeetingForm.getEstimatePeople()) && !RegexUtils.checkDigit2(applyMeetingForm.getEstimatePeople())) {
            return ActionResult.fail("预计人数只能输入正整数");
        }
        if (applyMeetingForm.getEstimatedAmount() != null && !RegexUtils.checkDecimals2(String.valueOf(applyMeetingForm.getEstimatedAmount()))) {
            return ActionResult.fail("预计金额必须大于0，最多精确小数点后两位");
        }
        ApplyMeetingEntity entity = JsonUtil.getJsonToBean(applyMeetingForm, ApplyMeetingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyMeetingForm.getStatus())) {
            applyMeetingService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        applyMeetingService.submit(id, entity, applyMeetingForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
