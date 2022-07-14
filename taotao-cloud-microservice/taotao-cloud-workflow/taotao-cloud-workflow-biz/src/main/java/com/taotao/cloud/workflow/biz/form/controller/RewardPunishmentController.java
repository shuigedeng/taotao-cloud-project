package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.RewardPunishmentEntity;
import jnpf.form.model.rewardpunishment.RewardPunishmentForm;
import jnpf.form.model.rewardpunishment.RewardPunishmentInfoVO;
import jnpf.form.service.RewardPunishmentService;
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
 * 行政赏罚单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "行政赏罚单", value = "RewardPunishment")
@RestController
@RequestMapping("/api/workflow/Form/RewardPunishment")
public class RewardPunishmentController {

    @Autowired
    private RewardPunishmentService rewardPunishmentService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;;

    /**
     * 获取行政赏罚单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取行政赏罚单信息")
    @GetMapping("/{id}")
    public ActionResult<RewardPunishmentInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        RewardPunishmentInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), RewardPunishmentInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            RewardPunishmentEntity entity = rewardPunishmentService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, RewardPunishmentInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建行政赏罚单
     *
     * @param rewardPunishmentForm 表单对象
     * @return
     */
    @ApiOperation("新建行政赏罚单")
    @PostMapping
    public ActionResult create(@RequestBody RewardPunishmentForm rewardPunishmentForm) throws WorkFlowException {
        RewardPunishmentEntity entity = JsonUtil.getJsonToBean(rewardPunishmentForm, RewardPunishmentEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(rewardPunishmentForm.getStatus())) {
            rewardPunishmentService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        rewardPunishmentService.submit(entity.getId(), entity,rewardPunishmentForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改行政赏罚单
     *
     * @param rewardPunishmentForm 表单对象
     * @param id                   主键
     * @return
     */
    @ApiOperation("修改行政赏罚单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody RewardPunishmentForm rewardPunishmentForm, @PathVariable("id") String id) throws WorkFlowException {
        RewardPunishmentEntity entity = JsonUtil.getJsonToBean(rewardPunishmentForm, RewardPunishmentEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(rewardPunishmentForm.getStatus())) {
            rewardPunishmentService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        rewardPunishmentService.submit(id, entity,rewardPunishmentForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
