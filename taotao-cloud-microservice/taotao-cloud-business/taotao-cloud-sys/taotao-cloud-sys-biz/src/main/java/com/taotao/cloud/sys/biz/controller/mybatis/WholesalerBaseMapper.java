///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.taotao.cloud.sys.biz.controller.mybatis;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import org.apache.ibatis.annotations.Options;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.ResultType;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.cursor.Cursor;
//import org.apache.ibatis.mapping.ResultSetType;
//import org.apache.ibatis.session.ResultHandler;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface WholesalerBaseMapper extends BaseMapper<WholesalerBase> {
//
//    /**
//     * 流式查询
//     *
//     * @param resultHandler
//     */
//    @ResultType(WholesalerBase.class)
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
//    @Select("""
//		select * from wholesaler_base
//	""")
//    void getBaseList(ResultHandler<WholesalerBase> resultHandler);
//
//    /**
//     * 流式查询 查询上一周的更新数据
//     *
//     * @param resultHandler
//     */
//    @ResultType(WholesalerBase.class)
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
//    @Select(
//            """
//		SELECT base.name        name,
//                 base.id          id,
//                 base.code,
//                 address,
//                 base.boss_name   bossName,
//                 base.boss_mobile bossMobile,
//                 latitude,
//                 longitude,
//                 base.create_time createTime,
//                 base.update_time updateTime
//          FROM wholesaler_base base
//          WHERE base.verify_status != 3 and base.status !=99 and YEARWEEK(date_format(base.update_time,'%Y-%m-%d'), 1) = YEARWEEK(now(), 1)-1
//                  and base.deleted=0 and base.level=2
//		""")
//    void getBaseWeekList(ResultHandler<WholesalerBase> resultHandler);
//
//    /** 获取指定状态奖励的结算记录 流式查询 */
//    @ResultType(TaskSettleLogDTO.class)
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
//    @Select(
//            """
//		 select settle.id as taskSettleLogId,
//          settle.task_code,
//          settle.task_cycle_id,
//          settle.satisfied,
//          task.id,
//          task.`code`,
//          task.task_type,
//          task.biz_code,
//          task.biz_name,
//          task.processor_code,
//          task.processor_type,
//          task.executor_target_code
//          task.enable_time,
//          cycle.end_time as cycleEndTime,
//          from task_settle_log as settle
//          left join task_cycle as cycle on cycle.id = settle.task_cycle_id
//          left join task on task.`code` = settle.task_code
//          where settle.reward_status in
//          <foreach collection="rewardStatuses" item="item" open="(" close=")" separator=",">
//              #{item}
//          </foreach>
//          and cycle.cycle_type = #{cycleType}
//          and cycle.end_time between #{starTime} and #{endTime}
//          and task.`enable` = 1
//          and task.deleted = 0
//		""")
//    void getNotIssuedCycleSettleLogs(
//            @Param("rewardStatuses") List<Integer> rewardStatuses,
//            @Param("cycleType") Integer cycleType,
//            @Param("starTime") LocalDateTime starTime,
//            @Param("endTime") LocalDateTime endTime,
//            ResultHandler<TaskSettleLogDTO> resultHandler);
//
//    @Select("select * from wholesaler_base base WHERE base.verify_status != 3 "
//            + "                and base.deleted=0 and base.level=2")
//    Cursor<WholesalerBase> scan();
//
//    @Select("${sql}")
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
//    @ResultType(TblMallOrder.class)
//    void dynamicSelectLargeData1(@Param("sql") String sql, ResultHandler<TblMallOrder> handler);
//
//    @Select("${sql}")
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
//    @ResultType(Map.class)
//    void dynamicSelectLargeData2(@Param("sql") String sql, ResultHandler<Map> handler);
//}
