package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowlaunch.FlowLaunchListVO;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
 * 流程发起
 *
 */
@Tag(tags = "流程发起", value = "FlowLaunch")
@RestController
@RequestMapping("/api/workflow/Engine/FlowLaunch")
public class FlowLaunchController {

    @Autowired
    private FlowEngineService flowEngineService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private FlowTaskNewService flowTaskNewService;

    /**
     * 获取流程发起列表
     *
     * @param paginationFlowTask
     * @return
     */
    @Operation("获取流程发起列表(带分页)")
    @GetMapping
    public ActionResult<PageListVO<FlowLaunchListVO>> list(PaginationFlowTask paginationFlowTask) {
        List<FlowTaskEntity> data = flowTaskService.getLaunchList(paginationFlowTask);
        List<FlowEngineEntity> engineList = flowEngineService.getFlowList(data.stream().map(t -> t.getFlowId()).collect(Collectors.toList()));
        List<FlowLaunchListVO> listVO = new LinkedList<>();
        for (FlowTaskEntity taskEntity : data) {
            //用户名称赋值
            FlowLaunchListVO vo = JsonUtil.getJsonToBean(taskEntity, FlowLaunchListVO.class);
            FlowEngineEntity entity = engineList.stream().filter(t -> t.getId().equals(taskEntity.getFlowId())).findFirst().orElse(null);
            if (entity != null) {
                vo.setFormData(entity.getFormData());
                vo.setFormType(entity.getFormType());
            }
            listVO.add(vo);
        }
        PaginationVO paginationVO = JsonUtil.getJsonToBean(paginationFlowTask, PaginationVO.class);
        return ActionResult.page(listVO, paginationVO);
    }

    /**
     * 删除流程发起
     *
     * @param id 主键值
     * @return
     */
    @Operation("删除流程发起")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) throws WorkFlowException {
        FlowTaskEntity entity = flowTaskService.getInfo(id);
        if (entity != null) {
            if (entity.getFlowType() == 1) {
                return ActionResult.fail("功能流程不能删除");
            }
            if (!FlowNature.ParentId.equals(entity.getParentId()) && StringUtil.isNotEmpty(entity.getParentId())) {
                return ActionResult.fail("子表数据不能删除");
            }
            flowTaskService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }

    /**
     * 待我审核催办
     *
     * @param id 主键值
     * @return
     */
    @Operation("发起催办")
    @PostMapping("/Press/{id}")
    public ActionResult press(@PathVariable("id") String id) throws WorkFlowException {
        boolean flag = flowTaskNewService.press(id);
        if (flag) {
            return ActionResult.success("催办成功");
        }
        return ActionResult.fail("未找到催办人");
    }

    /**
     * 撤回流程发起
     * 注意：在撤销流程时要保证你的下一节点没有处理这条记录；如已处理则无法撤销流程。
     *
     * @param id              主键值
     * @param flowHandleModel 经办记录
     * @return
     */
    @Operation("撤回流程发起")
    @PutMapping("/{id}/Actions/Withdraw")
    public ActionResult revoke(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(id);
        FlowModel flowModel = JsonUtil.getJsonToBean(flowHandleModel, FlowModel.class);
        flowTaskNewService.revoke(flowTaskEntity, flowModel);
        return ActionResult.success("撤回成功");
    }
}
