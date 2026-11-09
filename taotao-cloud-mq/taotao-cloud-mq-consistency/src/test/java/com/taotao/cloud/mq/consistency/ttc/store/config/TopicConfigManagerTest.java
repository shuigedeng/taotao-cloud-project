package com.taotao.cloud.mq.consistency.ttc.store.config;

import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * topic 配置后端的落盘/读回语义（不起 broker，只量这个类自己）。
 * <p>
 * 端到端那一层由 {@code TopicConfigRestartE2ETest} 量；这里量的是它的四条底座性质：
 * <ol>
 *   <li>变更之后换一个实例、同一个目录，字段逐项回来（含 perm/order/unit/sysFlag）；</li>
 *   <li>盘上那份是内存表的<b>镜像</b>：替换掉的东西两边一起消失，不许只清内存；</li>
 *   <li>一行坏了只丢那一条，不牵连邻居；</li>
 *   <li>写是原子的（.tmp 换名），残留的 .tmp 不算内容来源。</li>
 * </ol>
 * 目录一律是 {@link TempDir}，不碰 {@code ~/store}。
 */
public class TopicConfigManagerTest {

    @TempDir
    Path tempDir;

    private TopicConfigManager newManager() {
        return new TopicConfigManager(root());
    }

    private String root() {
        return tempDir.resolve("store").toString();
    }

    private File configFile() {
        return new File(root(), TopicConfigManager.TOPIC_CONFIG_FILE_NAME);
    }

    // ==================== 1. 空目录 / 幂等 ====================

    @Test
    @DisplayName("空目录起来是一张空表, 重复 start() 不重复读也不报错")
    public void startingWithAnEmptyDirectoryYieldsAnEmptyTableAndIsIdempotent() {
        TopicConfigManager mgr = newManager();
        assertFalse(configFile().exists(), "还没写过就不该有文件");
        mgr.start();
        mgr.start();
        assertEquals(0, mgr.size());
        assertNull(mgr.selectTopicConfig("NEVER"));
        assertTrue(mgr.getAllTopicConfigs().isEmpty());
    }

    // ==================== 2. 字段逐项跨实例 ====================

    @Test
    @DisplayName("写进去的每条配置换一个实例还能读回来, 且六个字段逐项对上")
    public void putThenReloadInAFreshInstanceKeepsEveryField() {
        TopicConfigManager first = newManager();
        first.start();
        TopicConfig config = new TopicConfig("W2dStoreTopic", 7, 3, TopicConfig.PERM_READ);
        config.setOrder(true);
        config.setUnit(true);
        config.setTopicSysFlag(5);
        assertNotNull(first.putTopicConfig(config), "写入口要把落进表里的那条回给调用方");
        assertEquals(1, first.size());

        TopicConfigManager second = newManager();
        second.start();
        TopicConfig got = second.selectTopicConfig("W2dStoreTopic");
        assertNotNull(got, "★ 换一个实例、同一个目录就必须读回来（这是「跨重启保留」那一半的底座）");
        assertEquals(7, got.getReadQueueNums());
        assertEquals(3, got.getWriteQueueNums());
        assertEquals(TopicConfig.PERM_READ, got.getPerm());
        assertTrue(got.isOrder());
        assertTrue(got.isUnit());
        assertEquals(5, got.getTopicSysFlag());
    }

    // ==================== 3. 盘是内存的镜像 ====================

    @Test
    @DisplayName("全量替换: incoming 里没有的条目在内存和盘上一起消失")
    public void replaceAllMakesTheDiskAnExactMirrorOfTheTable() throws IOException {
        TopicConfigManager mgr = newManager();
        mgr.start();
        mgr.putTopicConfig(new TopicConfig("Gone", 4, 4, TopicConfig.PERM_READ_WRITE));
        mgr.putTopicConfig(new TopicConfig("Kept", 4, 4, TopicConfig.PERM_READ_WRITE));

        Map<String, TopicConfig> incoming = new HashMap<>();
        incoming.put("Fresh", new TopicConfig("Fresh", 9, 9, TopicConfig.PERM_READ_WRITE));
        mgr.replaceAllTopicConfigs(incoming);

        assertEquals(1, mgr.size(), "内存侧: 只剩替换进来的那一条");
        assertNull(mgr.selectTopicConfig("Gone"));
        List<String> lines = Files.readAllLines(configFile().toPath(), StandardCharsets.UTF_8);
        assertEquals(1, lines.size(), "盘上侧: 一行一条, 换掉的不许还留着");
        assertTrue(lines.get(0).contains("\"topicName\":\"Fresh\""), "实际=" + lines);

        TopicConfigManager reboot = newManager();
        reboot.start();
        assertEquals(1, reboot.size(), "读回来也只能是这一份");
        assertEquals(9, reboot.selectTopicConfig("Fresh").getReadQueueNums());
        assertNull(reboot.selectTopicConfig("Gone"), "旧的那条如果还从盘上读回来, 说明替换没落盘");
    }

    @Test
    @DisplayName("null / 空 incoming: 表清空, 盘也清空（不许读到上一份）")
    public void replaceAllWithNothingEmptiesBothSides() throws IOException {
        TopicConfigManager mgr = newManager();
        mgr.start();
        mgr.putTopicConfig(new TopicConfig("A", 1, 1, TopicConfig.PERM_READ_WRITE));
        mgr.replaceAllTopicConfigs(null);
        assertEquals(0, mgr.size());
        assertEquals(0, Files.readAllLines(configFile().toPath(), StandardCharsets.UTF_8).size());

        TopicConfigManager reboot = newManager();
        reboot.start();
        assertEquals(0, reboot.size());
    }

    // ==================== 4. 坏行不牵连 ====================

    @Test
    @DisplayName("盘上有一行是半截 JSON: 只丢那一条, 其余照读")
    public void damagedLinesAreSkippedWithoutLosingTheirNeighbours() throws IOException {
        TopicConfigManager mgr = newManager();
        mgr.start();
        mgr.putTopicConfig(new TopicConfig("Good1", 2, 2, TopicConfig.PERM_READ_WRITE));
        mgr.putTopicConfig(new TopicConfig("Good2", 3, 3, TopicConfig.PERM_READ_WRITE));

        List<String> lines = new ArrayList<>();
        lines.add("{\"topicName\":\"Good1\",\"readQueueNums\":2,\"writeQueueNums\":2,\"perm\":6}");
        lines.add("{\"topicName\":\"Truncated\"");
        lines.add("这不是 JSON");
        lines.add("");
        lines.add("{\"topicName\":\"Good2\",\"readQueueNums\":3,\"writeQueueNums\":3,\"perm\":6}");
        Files.write(configFile().toPath(), String.join("\n", lines).getBytes(StandardCharsets.UTF_8));

        TopicConfigManager reboot = newManager();
        reboot.start();
        assertEquals(2, reboot.size(), "两条好的都要回来, 实测=" + reboot.getAllTopicConfigs().keySet());
        assertNotNull(reboot.selectTopicConfig("Good1"));
        assertNotNull(reboot.selectTopicConfig("Good2"));
        assertNull(reboot.selectTopicConfig("Truncated"));
    }

    // ==================== 5. 原子写不留 .tmp ====================

    @Test
    @DisplayName("每次变更之后目录里只剩正式文件, 不留 .tmp; 名字没带 topic 的写入不动表")
    public void persistLeavesNoTempFileAndBadWritesChangeNothing() {
        TopicConfigManager mgr = newManager();
        mgr.start();
        mgr.putTopicConfig(new TopicConfig("T1", 1, 1, TopicConfig.PERM_READ_WRITE));
        mgr.putTopicConfig(new TopicConfig("T2", 2, 2, TopicConfig.PERM_READ_WRITE));
        mgr.flush();

        File dir = new File(root());
        String[] leftovers = dir.list(new java.io.FilenameFilter() {
            @Override
            public boolean accept(File parent, String name) {
                return name.endsWith(".tmp");
            }
        });
        assertNotNull(leftovers);
        assertEquals(0, leftovers.length, "原子写之后不该留 .tmp: " + java.util.Arrays.toString(leftovers));
        assertTrue(configFile().isFile());

        int before = mgr.size();
        assertNull(mgr.putTopicConfig(null), "null 配置不许进表");
        assertNull(mgr.putTopicConfig(new TopicConfig(null)), "没有名字的 topic 不许进表");
        assertNull(mgr.putTopicConfig(new TopicConfig("", 1, 1, TopicConfig.PERM_READ_WRITE)),
                "空名字的 topic 不许进表");
        assertEquals(before, mgr.size(), "被拒的写入不许动已经记下的表");
        assertEquals(2, newManager2().size(), "盘上也还是那两条");
    }

    private TopicConfigManager newManager2() {
        TopicConfigManager mgr = newManager();
        mgr.start();
        return mgr;
    }

    // ==================== 6. clear 与关机落盘 ====================

    @Test
    @DisplayName("clear() 之后盘上也是空的; shutdown() 不吞掉任何一条已写的配置")
    public void clearEmptiesTheFileToo() throws IOException {
        TopicConfigManager mgr = newManager();
        mgr.start();
        mgr.putTopicConfig(new TopicConfig("W2dClear", 4, 4, TopicConfig.PERM_READ_WRITE));
        mgr.clear();
        assertEquals(0, mgr.size());
        assertEquals(0, Files.readAllLines(configFile().toPath(), StandardCharsets.UTF_8).size());

        TopicConfigManager another = newManager();
        another.start();
        another.putTopicConfig(new TopicConfig("W2dKeep", 6, 6, TopicConfig.PERM_READ_WRITE));
        another.shutdown();
        assertTrue(new File(root(), TopicConfigManager.TOPIC_CONFIG_FILE_NAME).isFile());
        assertEquals(1, newManager2().size(), "关机之后那条配置必须还在盘上");
    }
}
