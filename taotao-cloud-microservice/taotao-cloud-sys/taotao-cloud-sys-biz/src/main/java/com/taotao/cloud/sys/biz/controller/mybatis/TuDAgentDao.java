//package com.taotao.cloud.sys.biz.controller.mybatis;
//
//import com.aegonthtf.fate.constant.CommonConstant;
//import com.aegonthtf.fate.entity.user.TuDAgent;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.apache.ibatis.annotations.*;
//import org.apache.ibatis.mapping.ResultSetType;
//import org.apache.ibatis.session.ResultHandler;
//
//import java.util.Date;
//
///**
// * 营销员维度表(TuDAgent)表数据库访问层
// *
// */
//@Mapper
//public interface TuDAgentDao extends BaseMapper<TuDAgent> {
//
//    //流式批量查询处理数据
////    @Select("select * from tu_d_agent")//测试用
//    @Select("select * from tu_d_agent t where AGENT_STATUS = 'Active' and lcd >= #{startTime} and lcd <= #{endTime}")
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = CommonConstant.BATCH_SIZE)
//    @ResultType(TuDAgent.class)
//    void getUserListByLcdBigData(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("handler") ResultHandler<TuDAgent> handler);
//
//    //流式批量查询处理数据
////    @Select("select * from tu_d_agent t where lcd <= #{date} LIMIT 10")//测试用
//    @Select("select * from tu_d_agent where AGENT_STATUS = 'Active'")
//    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = CommonConstant.BATCH_SIZE)
//    @ResultType(TuDAgent.class)
//    void getUserListBigData(@Param("handler") ResultHandler<TuDAgent> handler);
//}
//
