package com.taotao.cloud.java.javaee.s2.c11_distributed.job.java.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

@Component
public class MyElasticJob implements SimpleJob {

    @Override
    public void execute(ShardingContext context) {
        switch (context.getShardingItem()) {
            case 0:
                System.out.println("执行0任务！！");
                break;
            case 1:
                System.out.println("执行1任务！！");
                break;
            case 2:
                System.out.println("执行2任务！！");
                break;
            // case n: ...
        }
    }
}
