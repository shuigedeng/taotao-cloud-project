package com.taotao.cloud.dolphinscheduler;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Random;

/**
 * Flink DataGen 示例：可控制生成速度和总条数
 */
public class DataGenToConsoleDemo {

    private static final Logger LOG = LoggerFactory.getLogger(DataGenToConsoleDemo.class);

    // ★★★ 在这里调整速度控制参数 ★★★
    private static final long RATE_PER_SECOND = 5;      // 每秒生成 5 条数据
    private static final long TOTAL_RECORDS = 100;      // 总共生成 100 条后作业结束 (设为 Long.MAX_VALUE 则无限生成)

    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境 (启用本地 Web UI，便于观察)
        Configuration config = new Configuration();
        // 本地 Web UI 端口默认是 8081
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); // 并行度设为1，便于观察顺序

        // 2. 创建速率限制器
        // 使用 perSecond 实现固定速率，无突发
        RateLimiterStrategy rateLimiter = RateLimiterStrategy.perSecond(RATE_PER_SECOND);

        // 3. 创建数据生成源
        DataGeneratorSource<UserClickLog> source = new DataGeneratorSource<>(
                new UserClickLogGenerator(),          // 生成函数
                TOTAL_RECORDS,                        // 总条数
                rateLimiter,                          // 速率限制
                Types.POJO(UserClickLog.class)
        );

        // 4. 添加 Source
        DataStream<UserClickLog> clickStream = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "DataGen Source")
                .name("User Click Source")
                .map(log -> {
                    log.setProcessed(true);
                    return log;
                })
                .name("Process Map");

        // 5. 输出到控制台 (并打印时间戳以观察速率)
        clickStream
                .map(log -> {
                    String msg = String.format("[%s] %s", Instant.now(), log);
                    LOG.info(msg); // 使用日志打印，会带时间戳
                    return log;
                })
                .name("Console with Timestamp");

        // 6. 执行作业
        env.execute("DataGen to Console Demo (Rate: " + RATE_PER_SECOND + "/s)");
    }

    // ============================================================
    // 数据模型与生成器 (与之前版本相同)
    // ============================================================
    public static class UserClickLog {
        public Long userId;
        public String pageUrl;
        public Long clickTime;
        public boolean isProcessed;

        public UserClickLog() {}

        public UserClickLog(Long userId, String pageUrl, Long clickTime) {
            this.userId = userId;
            this.pageUrl = pageUrl;
            this.clickTime = clickTime;
            this.isProcessed = false;
        }

        public void setProcessed(boolean processed) { isProcessed = processed; }

        @Override
        public String toString() {
            return "UserClickLog{" +
                    "userId=" + userId +
                    ", pageUrl='" + pageUrl + '\'' +
                    ", clickTime=" + Instant.ofEpochMilli(clickTime) +
                    ", isProcessed=" + isProcessed +
                    '}';
        }
    }

    public static class UserClickLogGenerator implements GeneratorFunction<Long, UserClickLog> {
        private static final String[] PAGES = {"/index.html", "/product/1001", "/product/2002", "/cart", "/checkout"};
        private final Random random = new Random();

        @Override
        public UserClickLog map(Long value) {
            Long userId = value % 1000 + 1;
            String page = PAGES[random.nextInt(PAGES.length)];
            Long clickTime = System.currentTimeMillis() - random.nextInt(3600_000);
            return new UserClickLog(userId, page, clickTime);
        }
    }
}
