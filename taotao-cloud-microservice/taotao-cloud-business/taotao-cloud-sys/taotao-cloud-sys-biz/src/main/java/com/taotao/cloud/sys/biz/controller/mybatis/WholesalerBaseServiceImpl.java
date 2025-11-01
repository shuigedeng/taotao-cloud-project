import com.taotao.cloud.sys.biz.controller.mybatis.WholesalerBase;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

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
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.cursor.Cursor;
//import org.apache.ibatis.session.ResultContext;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.support.TransactionTemplate;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class WholesalerBaseServiceImpl extends ServiceImpl<WholesalerBaseMapper, WholesalerBase>
//        implements WholesalerBaseService {
//
//    private final SqlSessionFactory sqlSessionFactory;
//    private final PlatformTransactionManager platformTransactionManager;
//
//    @Override
//    @Transactional(readOnly = true)
//    public void fixReuseSendMsgNew() {
//        this.baseMapper.getBaseWeekList(resultContext -> {
//            WholesalerBase base = resultContext.getResultObject();
//            try {
//                // 逻辑代码
//                log.info("base-->{}", base);
//            } catch (Exception e) {
//                log.error("XX发生错误,原因 = {}", e.getMessage());
//                LogUtils.error(e);
//            }
//        });
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public void scheduleGenerate() {
//
//        // 流式查询
//        // 获取指定状态奖励的[月周期]结算记录
//        LocalDateTime now = LocalDateTime.now();
//        // this.baseMapper.getNotIssuedCycleSettleLogs(
//        //        Arrays.asList(RewardLogStatusEnum.TO_BE_ISSUED.getCode(),
//        // RewardLogStatusEnum.MONTH_CYCLE_FAIL.getCode()),
//        //        TaskCycleTypeEnum.MONTH.getCode(),
//        //        now.minusMonths(1),
//        //        now,
//        //        (resultContext) -> {
//        //            TaskSettleLogDTO taskSettleLog = resultContext.getResultObject();
//        //            try {
//        //
//        //                if (taskSettleLog.getEnableTime() != null
//        //                        &&
//        // taskSettleLog.getCycleEndTime().isBefore(taskSettleLog.getEnableTime())
//        //                ) {
//        //                    // 如果结算月周期的结束时间 < 启用时间
//        //                    // 不会发奖，并且改变相关记录的状态
//        //                    this.taskRewardLogService.updateNoIncome(taskSettleLog);
//        //                } else {
//        //                    this.taskRewardLogService.awardPrizes(taskSettleLog);
//        //                }
//        //
//        //            } catch (Exception e) {
//        //                log.error("发奖发生错误 = {}", taskSettleLog, e);
//        //            }
//        //
//        //        }
//        // );
//
//    }
//
//    @Override
//    public void cursorTest() {
//        try (SqlSession sqlSession = this.sqlSessionFactory.openSession()) {
//            Cursor<WholesalerBase> cursors =
//                    sqlSession.getMapper(WholesalerBaseMapper.class).scan();
//            cursors.forEach(base -> {
//                // 业务需求
//                LogUtils.info("base = " + base);
//            });
//        }
//    }
//
//    @Override
//    public void transactionTemplateTest() {
//        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
//
//        transactionTemplate.execute(status -> {
//            try (Cursor<WholesalerBase> cursor = this.baseMapper.scan()) {
//                cursor.forEach(base -> {
//                    // 需求代码
//                    LogUtils.info("base = " + base);
//                });
//            } catch (Exception e) {
//                LogUtils.error(e);
//            }
//            return null;
//        });
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public void transactionalTest() {
//        try (Cursor<WholesalerBase> cursor = this.baseMapper.scan()) {
//            cursor.forEach(System.out::println);
//        } catch (Exception e) {
//            LogUtils.error(e);
//        }
//    }
//
//    // 流式查询中满1000条数据清空
//    @Transactional(readOnly = true)
//    public void test(Set<String> codes) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start("poc");
//        int num = 1000;
//        Set<String> pocMiddleIdList = new HashSet<>();
//        Map<String, String> pocMiddleIdOrgMap = new HashMap<>();
//        // this.pocBaseInfoMapper.getPocBaseInfoList(codes, (resultContext) -> {
//        //	PocBaseInfo resultObject = resultContext.getResultObject();
//        //	LogUtils.info(JSONUtil.toJsonStr(resultObject));
//        //	pocMiddleIdList.add(resultObject.getPocMiddleId());
//        //	pocMiddleIdOrgMap.put(resultObject.getPocMiddleId(), resultObject.getOrgCode());
//        //	if (pocMiddleIdList.size() == num) {
//        //		pocMiddleIdList.clear();
//        //
//        //	}
//        // });
//        stopWatch.stop();
//        // 测试进行时间
//        LogUtils.info(stopWatch.prettyPrint());
//    }
//
//    // 这是每批处理的大小
//    private static final int BATCH_SIZE = 1000;
//    private int size;
//    // 存储每批数据的临时容器
//    private List<TblMallOrder> mallOrders;
//
//    public void getLargeData1() {
//        String sql = "select * from t_mall_order";
//        this.baseMapper.dynamicSelectLargeData1(sql, new ResultHandler<TblMallOrder>() {
//            @Override
//            public void handleResult(ResultContext<? extends TblMallOrder> resultContext) {
//                TblMallOrder tblMallOrder = resultContext.getResultObject();
//                LogUtils.info(tblMallOrder);
//            }
//        });
//    }
//
//    public void getLargeData2() {
//        String sql = "select * from t_mall_order";
//        this.baseMapper.dynamicSelectLargeData1(sql, new ResultHandler<TblMallOrder>() {
//            @Override
//            public void handleResult(ResultContext<? extends TblMallOrder> resultContext) {
//                TblMallOrder tblMallOrder = resultContext.getResultObject();
//                LogUtils.info(tblMallOrder);
//                // 你可以看自己的项目需要分批进行处理或者单个处理，这里以分批处理为例
//                mallOrders.add(tblMallOrder);
//                size++;
//                if (size == BATCH_SIZE) {
//                    handle();
//                }
//            }
//        });
//        // 用来完成最后一批数据处理
//        handle();
//    }
//    /** 数据处理 */
//    @Transactional
//    public void handle() {
//        try {
//            // 在这里可以对你获取到的批量结果数据进行需要的业务处理
//        } catch (Exception e) {
//            LogUtils.error(e);
//        } finally {
//            // 处理完每批数据后后将临时清空
//            size = 0;
//            mallOrders.clear();
//        }
//    }
//
//	/**
//	 * mybatis的所谓流式查询，就是服务端程序查询数据的过程中，与远程数据库一直保持连接，不断的去数据库拉取数据，
//	 * 提交事务并关闭sqlsession后，数据库连接断开，停止数据拉取，需要注意的是使用这种方式，需要自己手动维护sqlsession和事务的提交。
//	 *
//	 * @Mapper
//	 * public interface PersonDao {
//	 *     Cursor<Person> selectByCursor();
//	 *     Integer queryCount();
//	 *
//	 * }
//	 *
//	 * <select id="selectByCursor" resultMap="personMap">
//	 *     select * from sys_person order by id desc
//	 * </select>
//	 * <select id="queryCount" resultType="java.lang.Integer">
//	 *     select count(*) from sys_person
//	 * </select>
//	 *
//	 * @throws InterruptedException
//	 */
//	@Override
//	public void getOneByAsync() throws InterruptedException {
//		new Thread(new Runnable() {
//			@SneakyThrows
//			@Override
//			public void run() {
//				//使用sqlSessionFactory打开一个sqlSession，在没有读取完数据之前不要提交事务或关闭sqlSession
//				log.info("----开启sqlSession");
//				SqlSession sqlSession = sqlSessionFactory.openSession();
//				try {
//					//获取到指定mapper
//					PersonDao mapper = sqlSession.getMapper(PersonDao.class);
//					//调用指定mapper的方法，返回一个cursor
//					Cursor<Person> cursor = mapper.selectByCursor();
//					//查询数据总量
//					Integer total = mapper.queryCount();
//					//定义一个list，用来从cursor中读取数据，每读取够1000条的时候，开始处理这批数据；
//					//当前批数据处理完之后，清空list，准备接收下一批次数据；直到大量的数据全部处理完；
//					List<Person> personList = new ArrayList<>();
//					int i = 0;
//					if (cursor != null) {
//						for (Person person : cursor) {
//							if (personList.size() < 1000) {
////                            log.info("----id:{},userName:{}", person.getId(), person.getUserName());
//								personList.add(person);
//							} else if (personList.size() == 1000) {
//								++i;
//								log.info("----{}、从cursor取数据达到1000条，开始处理数据", i);
//								log.info("----处理数据中...");
//								Thread.sleep(1000);//休眠1s模拟处理数据需要消耗的时间；
//								log.info("----{}、从cursor中取出的1000条数据已经处理完毕", i);
//								personList.clear();
//								personList.add(person);
//							}
//							if (total == (cursor.getCurrentIndex() + 1)) {
//								++i;
//								log.info("----{}、从cursor取数据达到1000条，开始处理数据", i);
//								log.info("----处理数据中...");
//								Thread.sleep(1000);//休眠1s模拟处理数据需要消耗的时间；
//								log.info("----{}、从cursor中取出的1000条数据已经处理完毕", i);
//								personList.clear();
//							}
//						}
//						if (cursor.isConsumed()) {
//							log.info("----查询sql匹配中的数据已经消费完毕！");
//						}
//					}
//					sqlSession.commit();
//					log.info("----提交事务");
//				}catch (Exception e){
//					LogUtils.error(e);
//					sqlSession.rollback();
//				}
//				finally {
//					if (sqlSession != null) {
//						//全部数据读取并且做好其他业务操作之后，提交事务并关闭连接；
//						sqlSession.close();
//						log.info("----关闭sqlSession");
//					}
//				}
//
//			}
//		}).start();
//	}
//
//}


