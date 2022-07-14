package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import jnpf.engine.entity.FlowCommentEntity;
import jnpf.engine.model.flowcomment.FlowCommentPagination;

/**
 * 流程评论
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
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
