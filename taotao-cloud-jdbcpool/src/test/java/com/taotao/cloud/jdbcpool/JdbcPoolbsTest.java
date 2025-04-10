package com.taotao.cloud.jdbcpool;

import com.taotao.cloud.jdbcpool.bs.JdbcPoolBs;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

/**
 *
 * @author shuigedeng
 * @since 1.2.0
 */
public class JdbcPoolbsTest {

    @Test
    public void bsTest() {
        JdbcPoolBs jdbcPoolBs = JdbcPoolBs.newInstance()
                .username("root")
                .password("123456")
                .url("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");

        DataSource pooled = jdbcPoolBs.pooled();
        DataSource unPooled = jdbcPoolBs.unPooled();
    }

}
