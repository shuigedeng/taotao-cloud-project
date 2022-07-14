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
import jnpf.form.entity.ArticlesWarehousEntity;
import jnpf.form.model.articleswarehous.ArticlesWarehousForm;
import jnpf.form.model.articleswarehous.ArticlesWarehousInfoVO;
import jnpf.form.service.ArticlesWarehousService;
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
 * 用品入库申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "用品入库申请表", value = "ArticlesWarehous")
@RestController
@RequestMapping("/api/workflow/Form/ArticlesWarehous")
public class ArticlesWarehousController {

    @Autowired
    private ArticlesWarehousService articlesWarehousService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取用品入库申请表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取用品入库申请表信息")
    @GetMapping("/{id}")
    public ActionResult<ArticlesWarehousInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ArticlesWarehousInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ArticlesWarehousInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ArticlesWarehousEntity entity = articlesWarehousService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ArticlesWarehousInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建用品入库申请表
     *
     * @param articlesWarehousForm 表单对象
     * @return
     */
    @ApiOperation("新建用品入库申请表")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ArticlesWarehousForm articlesWarehousForm) throws WorkFlowException {
        if (articlesWarehousForm.getEstimatePeople() != null && StringUtil.isNotEmpty(articlesWarehousForm.getEstimatePeople()) && !RegexUtils.checkDigit2(articlesWarehousForm.getEstimatePeople())) {
            return ActionResult.fail("数量只能输入正整数");
        }
        ArticlesWarehousEntity entity = JsonUtil.getJsonToBean(articlesWarehousForm, ArticlesWarehousEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(articlesWarehousForm.getStatus())) {
            articlesWarehousService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        articlesWarehousService.submit(entity.getId(), entity, articlesWarehousForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改用品入库申请表
     *
     * @param articlesWarehousForm 表单对象
     * @param id                   主键
     * @return
     */
    @ApiOperation("修改用品入库申请表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ArticlesWarehousForm articlesWarehousForm, @PathVariable("id") String id) throws WorkFlowException {
        if (articlesWarehousForm.getEstimatePeople() != null && StringUtil.isNotEmpty(articlesWarehousForm.getEstimatePeople()) && !RegexUtils.checkDigit2(articlesWarehousForm.getEstimatePeople())) {
            return ActionResult.fail("数量只能输入正整数");
        }
        ArticlesWarehousEntity entity = JsonUtil.getJsonToBean(articlesWarehousForm, ArticlesWarehousEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(articlesWarehousForm.getStatus())) {
            articlesWarehousService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        articlesWarehousService.submit(id, entity, articlesWarehousForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
