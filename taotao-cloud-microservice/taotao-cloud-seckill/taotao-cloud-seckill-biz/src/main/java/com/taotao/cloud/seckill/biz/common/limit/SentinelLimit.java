package com.taotao.cloud.seckill.biz.common.limit;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Sentinel 限流
 */
public class SentinelLimit {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //创建线程池  调整队列数 拒绝服务
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000));

    public static void main(String[] args) {
        // 配置规则
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("skill");
        // QPS 不得超出 50
        rule.setCount(50);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        // 加载规则
        FlowRuleManager.loadRules(rules);
        // 下面开始运行被限流作用域保护的代码
        int skillNum = 10;
        final CountDownLatch latch = new CountDownLatch(skillNum);//N个购买者
        AtomicInteger count = new AtomicInteger(0);
        for(int i=0;i<skillNum;i++){
            Runnable task = () -> {
                Entry entry = null;
                try {
                    entry = SphU.entry("skill");
                    System.out.println("秒杀成功");
                    count.getAndIncrement();
                } catch (BlockException e) {
                    System.out.println("blocked");
                } finally {
                    if (entry != null) {
                        entry.exit();
                    }
                }
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            System.out.println("秒杀成功数量"+count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
