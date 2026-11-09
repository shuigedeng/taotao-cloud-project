package com.taotao.cloud.mq.consistency.ttc.common.testsupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 简易性能基准测试框架 — 替代 JMH, 不引入额外依赖。
 * <p>
 * 用法:
 * <pre>
 *   BenchResult r = BenchResult.of("JsonCodec", "encode", 100000, () -> { ... });
 *   r.printToConsole();
 *   r.writeToFile(Paths.get("target/perf-reports/bench.txt"));
 * </pre>
 */
public class BenchResult {

    private final String category;
    private final String operation;
    private final int iterations;
    private final long elapsedNanos;
    private final int warmupRounds;

    private BenchResult(String category, String operation, int iterations, long elapsedNanos, int warmupRounds) {
        this.category = category;
        this.operation = operation;
        this.iterations = iterations;
        this.elapsedNanos = elapsedNanos;
        this.warmupRounds = warmupRounds;
    }

    /**
     * 执行一次性能测试。
     *
     * @param category   分类名 (如 "JsonCodec")
     * @param operation  操作名 (如 "encode")
     * @param iterations 迭代次数
     * @param warmup     预热轮数
     * @param task       被测代码
     */
    public static BenchResult of(String category, String operation, int iterations, int warmup, Runnable task) {
        for (int i = 0; i < warmup; i++) {
            task.run();
        }
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            task.run();
        }
        long elapsed = System.nanoTime() - start;
        return new BenchResult(category, operation, iterations, elapsed, warmup);
    }

    /**
     * 跑 N 次取最佳 (用于减少 GC 抖动影响)。
     *
     * @param runs        总运行次数, 实际只取最快的那次 (排除首次 JIT warmup)
     * @param iterations  单次迭代次数
     * @param warmup      单次预热轮数
     * @return 所有 runs 中 elapsedNanos 最小的结果
     */
    public static BenchResult runBest(String category, String operation, int runs, int iterations, int warmup, Runnable task) {
        BenchResult best = null;
        for (int r = 0; r < runs; r++) {
            BenchResult current = of(category, operation + "-run" + (r + 1), iterations, warmup, task);
            if (best == null || current.elapsedNanos < best.elapsedNanos) {
                best = new BenchResult(category, operation, iterations, current.elapsedNanos, warmup);
            }
        }
        return best;
    }

    /**
     * 跑 3 次取最佳 (Spec 默认要求)。
     */
    public static BenchResult runBest3(String category, String operation, int iterations, int warmup, Runnable task) {
        return runBest(category, operation, 3, iterations, warmup, task);
    }

    public double opsPerSecond() {
        return (double) iterations / (elapsedNanos / 1_000_000_000.0);
    }

    public double msPerOp() {
        return (elapsedNanos / 1_000_000.0) / iterations;
    }

    public double usPerOp() {
        return (elapsedNanos / 1_000.0) / iterations;
    }

    public void printToConsole() {
        System.out.printf("[BENCH] [%s] %s: %,d iters, %,d ms total, %.3f us/op, %.0f ops/s%n",
                category, operation, iterations, elapsedNanos / 1_000_000, usPerOp(), opsPerSecond());
    }

    public void writeToFile(Path filePath) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = String.format("[%s] [%s] %s: iters=%,d elapsed=%,dms us/op=%.3f ops/s=%.0f%n",
                ts, category, operation, iterations, elapsedNanos / 1_000_000, usPerOp(), opsPerSecond());
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("[BENCH] Failed to write perf report: " + e.getMessage());
        }
    }

    public void assertBelowThreshold(double maxUsPerOp, String description) {
        double actual = usPerOp();
        if (actual >= maxUsPerOp) {
            throw new AssertionError(String.format(
                    "[BENCH FAIL] %s: %.3f us/op >= threshold %.3f us/op (ops/s=%.0f)",
                    description, actual, maxUsPerOp, opsPerSecond()));
        }
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public int getIterations() {
        return iterations;
    }
}
