package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowmonitor.FlowMonitorListVO;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowDeleteModel;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程监控
 */
@Tag(tags = "流程监控", value = "FlowMonitor")
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
    @Operation("获取流程监控列表")
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
    @Operation("批量删除流程监控")
    @DeleteMapping
    public ActionResult delete(@RequestBody FlowDeleteModel deleteModel) throws WorkFlowException {
        String[] taskId = deleteModel.getIds().split(",");
        flowTaskService.delete(taskId);
        return ActionResult.success(MsgCode.SU003.get());
    }

}
