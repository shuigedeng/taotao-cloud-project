package com.taotao.cloud.mq.consistency.ttc.nameserver;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * NamesrvConfig 单元测试
 */
public class NamesrvConfigTest {

    @Test
    public void testDefaultKvConfigPath() {
        NamesrvConfig c = new NamesrvConfig();
        String path = c.getKvConfigPath();
        assertNotNull(path);
        // 默认路径包含 user.home + zmq + namesrv + kvConfig.json
        String userHome = System.getProperty("user.home");
        assertEquals(true, path.startsWith(userHome));
        assertEquals(true, path.endsWith("kvConfig.json"));
    }

    @Test
    public void testSetKvConfigPath() {
        NamesrvConfig c = new NamesrvConfig();
        c.setKvConfigPath("/tmp/test/kvConfig.json");
        assertEquals("/tmp/test/kvConfig.json", c.getKvConfigPath());
    }

    @Test
    public void testSetNullKvConfigPath() {
        NamesrvConfig c = new NamesrvConfig();
        c.setKvConfigPath(null);
        assertNull(c.getKvConfigPath());
    }

    @Test
    public void testPathContainsZmqSegment() {
        // 确保默认路径仍能识别 zmq 集群
        File f = new File(new NamesrvConfig().getKvConfigPath());
        assertEquals(true, f.getPath().contains("zmq"));
    }
}
