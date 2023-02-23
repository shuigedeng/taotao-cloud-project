package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCommentEntity;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentPagination;

import java.util.List;

/**
 * 流程评论
 *
 */
public interface FlowCommentService extends IService<FlowCommentEntity> {

    /**
     * 列表
     *
     * @param pagination 请求参数
     * @return
     */
    List<FlowCommentEntity> getlist(FlowCommentPagination pagination);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    FlowCommentEntity getInfo(String id);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(FlowCommentEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    void update(String id, FlowCommentEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     * @return
     */
    void delete(FlowCommentEntity entity);
}
