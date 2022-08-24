package com.taotao.cloud.retry.strategy;

import com.taotao.cloud.common.utils.log.LogUtil;
import io.github.itning.retry.strategy.block.BlockStrategy;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 自旋锁的实现, 不响应线程中断
 */
public class SpinBlockStrategy implements BlockStrategy {

    @Override
    public void block(long sleepTime) throws InterruptedException {

        LocalDateTime startTime = LocalDateTime.now();

        long start = System.currentTimeMillis();
        long end = start;
		LogUtil.info("[SpinBlockStrategy]...begin wait.");

        while (end - start <= sleepTime) {
            end = System.currentTimeMillis();
        }

        //使用Java8新增的Duration计算时间间隔
        Duration duration = Duration.between(startTime, LocalDateTime.now());

		LogUtil.info("[SpinBlockStrategy]...end wait.duration={}", duration.toMillis());

    }
}
