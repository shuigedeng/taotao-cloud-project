package com.taotao.cloud.workflow.biz.engine.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.base.Pagination;
import jnpf.base.UserInfo;
import jnpf.base.vo.PageListVO;
import jnpf.base.vo.PaginationVO;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowDelegateEntity;
import jnpf.engine.model.flowdelegate.FlowDelegatListVO;
import jnpf.engine.model.flowdelegate.FlowDelegateCrForm;
import jnpf.engine.model.flowdelegate.FlowDelegateInfoVO;
import jnpf.engine.model.flowdelegate.FlowDelegateUpForm;
import jnpf.engine.service.FlowDelegateService;
import jnpf.exception.DataException;
import jnpf.util.JsonUtil;
import jnpf.util.JsonUtilEx;
import jnpf.util.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程委托
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "流程委托", value = "FlowDelegate")
@RestController
@RequestMapping("/api/workflow/Engine/FlowDelegate")
public class FlowDelegateController {

    @Autowired
    private FlowDelegateService flowDelegateService;
    @Autowired
    private UserProvider userProvider;

    /**
     * 获取流程委托列表
     *
     * @param pagination
     * @return
     */
    @ApiOperation("获取流程委托列表")
    @GetMapping
    public ActionResult<PageListVO<FlowDelegatListVO>> list(Pagination pagination) {
        List<FlowDelegateEntity> list = flowDelegateService.getList(pagination);
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        List<FlowDelegatListVO> listVO = JsonUtil.getJsonToList(list, FlowDelegatListVO.class);
        return ActionResult.page(listVO, paginationVO);
    }

    /**
     * 获取流程委托信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取流程委托信息")
    @GetMapping("/{id}")
    public ActionResult<FlowDelegateInfoVO> info(@PathVariable("id") String id) throws DataException {
        FlowDelegateEntity entity = flowDelegateService.getInfo(id);
        FlowDelegateInfoVO vo = JsonUtilEx.getJsonToBeanEx(entity, FlowDelegateInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 新建流程委托
     *
     * @param flowDelegateCrForm 实体对象
     * @return
     */
    @ApiOperation("新建流程委托")
    @PostMapping
    public ActionResult create(@RequestBody @Valid FlowDelegateCrForm flowDelegateCrForm) {
        FlowDelegateEntity entity = JsonUtil.getJsonToBean(flowDelegateCrForm, FlowDelegateEntity.class);
        UserInfo userInfo = userProvider.get();
        if(userInfo.getUserId().equals(entity.getFTouserid())){
            return ActionResult.fail("委托人为自己，委托失败");
        }
        flowDelegateService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新流程委托
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("更新流程委托")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid FlowDelegateUpForm flowDelegateUpForm) {
        FlowDelegateEntity entity = JsonUtil.getJsonToBean(flowDelegateUpForm, FlowDelegateEntity.class);
        UserInfo userInfo = userProvider.get();
        if(userInfo.getUserId().equals(entity.getFTouserid())){
            return ActionResult.fail("委托人为自己，委托失败");
        }
        boolean flag = flowDelegateService.update(id, entity);
        if (flag == false) {
            return ActionResult.success(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除流程委托
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("删除流程委托")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        FlowDelegateEntity entity = flowDelegateService.getInfo(id);
        if (entity != null) {
            flowDelegateService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }
}
