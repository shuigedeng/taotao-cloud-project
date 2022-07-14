package com.taotao.cloud.workflow.biz.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowTaskListModel;
import org.apache.ibatis.annotations.Param;

/**
 * 流程任务
 *
 */
public interface FlowTaskMapper extends BaseMapper<FlowTaskEntity> {
    /**
     * 已办事宜
     * @param map 参数
     * @return
     */
    List<FlowTaskListModel> getTrialList(@Param("map") Map<String, Object> map);

    /**
     * 抄送事宜
     * @param sql 自定义sql语句
     * @return
     */
    List<FlowTaskListModel> getCirculateList(@Param("sql") String sql);

    /**
     * 待办事宜
     * @param sql 自定义sql语句
     * @return
     */
    List<FlowTaskListModel> getWaitList(@Param("sql") String sql);
}
