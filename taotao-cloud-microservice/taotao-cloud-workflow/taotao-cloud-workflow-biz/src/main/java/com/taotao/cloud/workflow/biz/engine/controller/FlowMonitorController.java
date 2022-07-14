package com.taotao.cloud.workflow.biz.engine.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import jnpf.base.ActionResult;
import jnpf.base.vo.PageListVO;
import jnpf.base.vo.PaginationVO;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowEngineEntity;
import jnpf.engine.entity.FlowTaskEntity;
import jnpf.engine.model.flowmonitor.FlowMonitorListVO;
import jnpf.engine.model.flowtask.FlowDeleteModel;
import jnpf.engine.model.flowtask.PaginationFlowTask;
import jnpf.engine.service.FlowEngineService;
import jnpf.engine.service.FlowTaskService;
import jnpf.engine.util.ServiceAllUtil;
import jnpf.exception.WorkFlowException;
import jnpf.permission.entity.UserEntity;
import jnpf.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程监控
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "流程监控", value = "FlowMonitor")
@RestController
@RequestMapping("/api/workflow/Engine/FlowMonitor")
public class FlowMonitorController {

    @Autowired
    private FlowEngineService flowEngineService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private ServiceAllUtil serviceUtil;

    /**
     * 获取流程监控列表
     *
     * @param paginationFlowTask
     * @return
     */
    @ApiOperation("获取流程监控列表")
    @GetMapping
    public ActionResult<PageListVO<FlowMonitorListVO>> list(PaginationFlowTask paginationFlowTask) {
        List<FlowTaskEntity> list = flowTaskService.getMonitorList(paginationFlowTask);
        List<FlowEngineEntity> engineList = flowEngineService.getFlowList(list.stream().map(t -> t.getFlowId()).collect(Collectors.toList()));
        List<UserEntity> userList = serviceUtil.getUserName(list.stream().map(t -> t.getCreatorUserId()).collect(Collectors.toList()));
        List<FlowMonitorListVO> listVO = new LinkedList<>();
        for (FlowTaskEntity taskEntity : list) {
            //用户名称赋值
            FlowMonitorListVO vo = JsonUtil.getJsonToBean(taskEntity, FlowMonitorListVO.class);
            UserEntity user = userList.stream().filter(t -> t.getId().equals(taskEntity.getCreatorUserId())).findFirst().orElse(null);
            vo.setUserName(user != null ? user.getRealName() + "/" + user.getAccount() : "");
            FlowEngineEntity engine = engineList.stream().filter(t -> t.getId().equals(taskEntity.getFlowId())).findFirst().orElse(null);
            if (engine != null) {
                vo.setFormData(engine.getFormData());
                vo.setFormType(engine.getFormType());
                listVO.add(vo);
            }
        }
        PaginationVO paginationVO = JsonUtil.getJsonToBean(paginationFlowTask, PaginationVO.class);
        return ActionResult.page(listVO, paginationVO);
    }

    /**
     * 批量删除流程监控
     *
     * @param deleteModel 主键
     * @return
     */
    @ApiOperation("批量删除流程监控")
    @DeleteMapping
    public ActionResult delete(@RequestBody FlowDeleteModel deleteModel) throws WorkFlowException {
        String[] taskId = deleteModel.getIds().split(",");
        flowTaskService.delete(taskId);
        return ActionResult.success(MsgCode.SU003.get());
    }

}
