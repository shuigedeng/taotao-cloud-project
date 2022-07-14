package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCommentEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowcomment.FlowCommentListVO;
import com.taotao.cloud.workflow.biz.engine.model.flowcomment.FlowCommentPagination;
import com.taotao.cloud.workflow.biz.engine.service.FlowCommentService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
 * 流程评论
 *
 */
@Tag(tags = "流程评论", value = "Comment")
@RestController
@RequestMapping("/api/workflow/Engine/FlowComment")
public class FlowCommentController {


    @Autowired
    private ServiceAllUtil serviceUtil;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private FlowCommentService flowCommentService;

    /**
     * 获取流程评论列表
     *
     * @param pagination
     * @return
     */
    @Operation("获取流程评论列表")
    @GetMapping
    public ActionResult list(FlowCommentPagination pagination) {
        List<FlowCommentEntity> list = flowCommentService.getlist(pagination);
        List<FlowCommentListVO> listVO = JsonUtil.getJsonToList(list, FlowCommentListVO.class);
        List<String> userId = list.stream().map(t -> t.getCreatorUserId()).collect(Collectors.toList());
        UserInfo userInfo = userProvider.get();
        List<UserEntity> userName = serviceUtil.getUserName(userId);
        for (FlowCommentListVO commentModel : listVO) {
            UserEntity userEntity = userName.stream().filter(t -> t.getId().equals(commentModel.getCreatorUserId())).findFirst().orElse(null);
            commentModel.setIsDel(commentModel.getCreatorUserId().equals(userInfo.getUserId()));
            commentModel.setCreatorUserName(userEntity != null ? userEntity.getRealName() : "");
            commentModel.setCreatorUserId(userEntity != null ? userEntity.getAccount() : "");
            if(userEntity != null){
                commentModel.setCreatorUserHeadIcon(UploaderUtil.uploaderImg(userEntity.getHeadIcon()));
            }
        }
        PaginationVO vo = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(listVO, vo);
    }

    /**
     * 获取流程评论信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取流程评论信息")
    @GetMapping("/{id}")
    public ActionResult info(@PathVariable("id") String id) {
        FlowCommentEntity entity = flowCommentService.getInfo(id);
        FlowCommentInfoVO vo = JsonUtil.getJsonToBean(entity, FlowCommentInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 新建流程评论
     *
     * @param commentForm 实体对象
     * @return
     */
    @Operation("新建流程评论")
    @PostMapping
    public ActionResult create(@RequestBody @Valid FlowCommentForm commentForm) throws DataException {
        FlowCommentEntity entity = JsonUtil.getJsonToBean(commentForm, FlowCommentEntity.class);
        flowCommentService.create(entity);
        return ActionResult.success(MsgCode.SU002.get());
    }

    /**
     * 更新流程评论
     *
     * @param id 主键值
     * @return
     */
    @Operation("更新流程评论")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid FlowCommentForm commentForm) throws DataException {
        FlowCommentEntity info = flowCommentService.getInfo(id);
        if (info != null) {
            FlowCommentEntity entity = JsonUtil.getJsonToBean(commentForm, FlowCommentEntity.class);
            flowCommentService.update(id, entity);
            return ActionResult.success(MsgCode.SU004.get());
        }
        return ActionResult.fail(MsgCode.FA002.get());
    }

    /**
     * 删除流程评论
     *
     * @param id 主键值
     * @return
     */
    @Operation("删除流程评论")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        FlowCommentEntity entity = flowCommentService.getInfo(id);
        if (entity.getCreatorUserId().equals(userProvider.get().getUserId())) {
            flowCommentService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.success(MsgCode.FA003.get());
    }

}
