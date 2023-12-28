/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.controller.mybatis;
//
// import cn.com.common.util.util.StringUtils;
// import com.aegonthtf.fate.constant.CommonConstant;
// import com.aegonthtf.fate.dao.secondary.TuDAgentDao;
// import com.aegonthtf.fate.entity.user.FateAgent;
// import com.aegonthtf.fate.entity.user.FateGoals;
// import com.aegonthtf.fate.entity.user.FateRecord;
// import com.aegonthtf.fate.entity.user.TuDAgent;
// import com.aegonthtf.fate.service.convert.TuDagentToFateAgentConverter;
// import com.aegonthtf.fate.service.convert.TuDagentToFateRecordConverter;
// import com.aegonthtf.fate.service.user.*;
// import com.aegonthtf.fate.util.JodaTimeUtils;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// import org.apache.commons.lang3.tuple.ImmutablePair;
// import org.joda.time.LocalDateTime;
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.util.CollectionUtils;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.Executor;
//
/// **
// * 营销员维度表(TuDAgent)表服务实现类
// *
// */
// @Service("tuDAgentService")
// public class TuDAgentServiceImpl extends ServiceImpl<TuDAgentDao, TuDAgent> implements
// TuDAgentService, InitializingBean {
//    @Autowired
//    public               FateAgentService      fateAgentService;
//    @Autowired
//    public               TuDAgentService       tuDAgentService;
//    @Autowired
//    private              FateBranchService     fateBranchService;
//    @Autowired
//    private              FateGoalsService      fateGoalsService;
//    @Autowired
//    private              FateApolloActService  fateApolloActService;
//    @Autowired
//    private              FateRecordService     fateRecordService;
//    @Autowired
//    private              AisuattendanceService aisuattendanceService;
//    @Autowired
//    private              AisuleaveService      aisuleaveService;
//    private              int                   size1;//计数器1
//    @Autowired
//    @Qualifier("tuDAgentServiceImpl1")
//    private              Executor              executor1;
//    private volatile     BlockingQueue         tuDAgentServiceImplQueue1; // 存储每批数据的临时容器
//    private              int                   size2;//计数器2
//    @Autowired
//    @Qualifier("tuDAgentServiceImpl2")
//    private              Executor              executor2;
//    private volatile     BlockingQueue         tuDAgentServiceImplQueue2; // 存储每批数据的临时容器
//
//    @Override
//    public void syncFateAgent(LocalDateTime staratDateTime) {
//        ImmutablePair<LocalDateTime, LocalDateTime> timeDay =
// JodaTimeUtils.criticalTimeDay(staratDateTime.toLocalDate());
//        this.baseMapper.getUserListByLcdBigData(timeDay.getLeft().toDate(),
// timeDay.getRight().toDate(),
//                resultContext -> {
//                    Optional.ofNullable(resultContext.getResultObject())
//                            .ifPresent(this.tuDAgentServiceImplQueue1::add);
//                    size1++;
//                    if (size1 >= CommonConstant.BATCH_SIZE) {
//                        this.handle1(staratDateTime);
//                    }
//                });
//        //用来完成最后一批数据处理
//        this.handle1(staratDateTime);
//    }
//
//    /**
//     * 数据处理
//     */
//    @Transactional
//    public void handle1(LocalDateTime staratDateTime) {
//        try {
//            // 在这里可以对你获取到的批量结果数据进行需要的业务处理
//            List<TuDAgent> tuDAgents1 = new ArrayList<>();
//            this.tuDAgentServiceImplQueue1.drainTo(tuDAgents1, CommonConstant.BATCH_SIZE);
//            if (CollectionUtils.isEmpty(tuDAgents1)) {
//                return;
//            }
//            List<FateAgent> fateAgents = new ArrayList<>();
//            String day = staratDateTime.toString("d");
//            tuDAgents1.parallelStream().forEach(item -> {
//                FateAgent fateAgent =
// this.fateAgentService.getFateAgentByAgentCode(item.getAgentCode());
//                FateAgent to;
//                if (Objects.isNull(fateAgent)) {
//                    to = TuDagentToFateAgentConverter.to(item, staratDateTime, new FateAgent());
//                } else {
////                to = TuDagentToFateAgentConverter.to(item, fateAgent);
//                    //有记录的更新AGENT_STATUS
//                    to = fateAgent;
//
// Optional.ofNullable(item.getAgentStatus()).ifPresent(fateAgent::setAgentStatus);
//                }
//                if (Objects.isNull(to)) {
//                    return;
//                }
//                //处理签约事件
//                this.fateApolloActService.contractEventHandling(staratDateTime, item);
//                //获取省一 二 三
//                if (StringUtils.isNotBlank(to.getBranchcode())) {
//                    FateAgent fateAgentOfBranch123 =
// this.fateBranchService.getFateBranchByBranchCode(to.getBranchcode());
//                    if (!Objects.isNull(fateAgentOfBranch123)) {
//
// Optional.ofNullable(fateAgentOfBranch123.getBrhFst()).ifPresent(to::setBrhFst);
//
// Optional.ofNullable(fateAgentOfBranch123.getBrhScd()).ifPresent(to::setBrhScd);
//
// Optional.ofNullable(fateAgentOfBranch123.getBrhThd()).ifPresent(to::setBrhThd);
//                    }
//                }
//                //获取目标APE 每月一号 二号更新 GAPE和目标新增人力gManpower
//                if (StringUtils.equals(day, "1") || StringUtils.equals(day, "2")) {
//                    FateGoals one = this.fateGoalsService.getOne(new QueryWrapper<FateGoals>()
//                            .lambda()
//                            .eq(FateGoals::getAgentCode, to.getAgentCode())
//                            .last(CommonConstant.LIMIT1)
//                    );
//                    Optional.ofNullable(one).ifPresent(itemFateGoals -> {
//                        //目标达成-APE
//                        to.setGApe(Objects.isNull(itemFateGoals.getGApe()) ? 0D : one.getGApe());
//                        //目标-新增人力
//                        to.setGManpower(Objects.isNull(itemFateGoals.getGManpower()) ? 0 :
// one.getGManpower());
//                    });
//                }
//                //达成-新增人力 aManpower
//                Integer aManpower =
// this.tuDAgentService.getFateAgentByAManpowerAndMonth(to.getAgentCode(), staratDateTime);
//                to.setAManpower(Objects.isNull(aManpower) ? 0 : aManpower);
//                fateAgents.add(to);
//            });
//            this.fateAgentService.saveOrUpDateBatchEntity(fateAgents);
//
//        } catch (Exception e) {
//            LogUtils.error(e);
//        } finally {
//            // 处理完每批数据后后将临时清空
//            size1 = 0;
//        }
//    }
//
//    @Override
//    public void initFateRecord(LocalDateTime staratDateTime) {
//        this.baseMapper.getUserListBigData(resultContext -> {
//            Optional.ofNullable(resultContext.getResultObject())
//                    .ifPresent(this.tuDAgentServiceImplQueue2::add);
//            size2++;
//            if (size2 >= CommonConstant.BATCH_SIZE) {
//                this.handle2(staratDateTime);
//            }
//        });
//        //用来完成最后一批数据处理
//        this.handle2(staratDateTime);
//    }
//
//    /**
//     * 数据处理
//     */
//    @Transactional
//    public void handle2(LocalDateTime staratDateTime) {
//        try {
//            // 在这里可以对你获取到的批量结果数据进行需要的业务处理
//            List<TuDAgent> tuDAgents2 = new ArrayList<>();
//            this.tuDAgentServiceImplQueue1.drainTo(tuDAgents2, CommonConstant.BATCH_SIZE);
//            if (CollectionUtils.isEmpty(tuDAgents2)) {
//                return;
//            }
//            List<FateRecord> fateRecords = new ArrayList<>();
//            tuDAgents2.parallelStream().forEach(item -> {
//                if (Objects.isNull(item)) {
//                    return;
//                }
//                FateRecord fateRecord =
// this.fateRecordService.getTodayByAgentCode(item.getAgentCode(), staratDateTime);
//                FateRecord to;
//                if (Objects.isNull(fateRecord)) {
//                    to = TuDagentToFateRecordConverter.to(item, staratDateTime, new FateRecord());
//                } else {
//                    to = TuDagentToFateRecordConverter.to(item, staratDateTime, fateRecord);
//                }
//                if (Objects.isNull(to)) {
//                    return;
//                }
//                //获取省一 二 三
//                if (StringUtils.isNotBlank(to.getBranchcode())) {
//                    FateAgent fateAgentOfBranch123 =
// this.fateBranchService.getFateBranchByBranchCode(to.getBranchcode());
//                    if (!Objects.isNull(fateAgentOfBranch123)) {
//
// Optional.ofNullable(fateAgentOfBranch123.getBrhFst()).ifPresent(to::setBrhFst);
//
// Optional.ofNullable(fateAgentOfBranch123.getBrhScd()).ifPresent(to::setBrhScd);
//
// Optional.ofNullable(fateAgentOfBranch123.getBrhThd()).ifPresent(to::setBrhThd);
//                    }
//                }
//                //判断工作日逻辑
//                //1.根据代理人工号查询aisuattendance表有无当天记录。
//                //SELECT COUNT(1) FROM AISUATTENDANCE T WHERE T.AI_REC_ENDDT IS NULL AND T.AI_AGID
// = '工号' AND T.AI_ATTENDANCE_DATE = '日期';
//                //2.若无记录，则当天非工作日，若有记录，查当前代理人是否请假。
//                //select count(1) from aisuleave where ai_agid='0000223106' and ai_leave_enddt
// >='20201230' and ai_leave_stdt <='20201230';
//                //3.若仍无记录，则当天是工作日。
//                String workFlag;
//                Integer count1 =
// this.aisuattendanceService.getWorkFlagByAgentCode(to.getAgentCode(), staratDateTime);
//                if (count1 <= 0) {
//                    workFlag = "N";
//                } else {
//                    Integer count2 =
// this.aisuleaveService.getWorkFlagByAgentCode(to.getAgentCode(), staratDateTime);
//                    workFlag = (count2 <= 0) ? "Y" : "N";
//                }
//                to.setWorkFlag(workFlag); //是否是工作日(Y:N)	每天7点批处理更新
//                Integer count =
// this.fateRecordService.getYesterdayFateRecordByAgentCode(to.getAgentCode());
//                to.setRecordDays(count); //连续打卡天数
//                fateRecords.add(to);
//            });
//            this.fateRecordService.saveOrUpDateBatchEntity(fateRecords);
//        } catch (Exception e) {
//            LogUtils.error(e);
//        } finally {
//            // 处理完每批数据后后将临时清空
//            size2 = 0;
//        }
//    }
//
//    //查询该工号当月 //当月达成-新增人力 aManpower
//    @Override
//    public Integer getFateAgentByAManpowerAndMonth(String agentCode, LocalDateTime staratDateTime)
// {
//        ImmutablePair<LocalDateTime, LocalDateTime> timeMonth =
// JodaTimeUtils.criticalTimeMonth(staratDateTime);
//        return this.count(new QueryWrapper<TuDAgent>().lambda()
//                .eq(TuDAgent::getRefferCode, agentCode)
//                .ge(TuDAgent::getFcd, timeMonth.left.toDate())
//                .le(TuDAgent::getFcd, timeMonth.right.toDate())
//        );
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        ThreadPoolTaskExecutor poolTaskExecutor1 = (ThreadPoolTaskExecutor) this.executor1;
//        ThreadPoolTaskExecutor poolTaskExecutor2 = (ThreadPoolTaskExecutor) this.executor2;
//        this.tuDAgentServiceImplQueue1 = poolTaskExecutor1.getThreadPoolExecutor().getQueue();
//        this.tuDAgentServiceImplQueue2 = poolTaskExecutor2.getThreadPoolExecutor().getQueue();
//    }
// }
//
