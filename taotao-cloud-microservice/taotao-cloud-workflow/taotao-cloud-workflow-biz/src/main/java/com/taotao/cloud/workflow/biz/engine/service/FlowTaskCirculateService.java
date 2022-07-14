package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import jnpf.engine.entity.FlowTaskCirculateEntity;

/**
 * 流程传阅
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface FlowTaskCirculateService extends IService<FlowTaskCirculateEntity> {

    /**
     * 删除（根据实例Id）
     *
     * @param taskId 任务主键
     * @return
     */
    void deleteByTaskId(String taskId);

    /**
     * 删除
     *
     * @param nodeId 节点主键
     * @return
     */
    void deleteByNodeId(String nodeId);

    /**
     * 创建
     *
     * @param entitys 实体对象
     * @return
     */
    void create(List<FlowTaskCirculateEntity> entitys);
}
