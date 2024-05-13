package com.github.houbb.thread.pool.datasource;

import com.github.houbb.thread.pool.bs.JdbcPoolBs;
import org.junit.Ignore;
import org.junit.Test;

import javax.sql.DataSource;

/**
 * <p> project: jdbc-pool-GenTest </p>
 * <p> create on 2020/7/18 9:25 </p>
 *
 * @author shuigedeng
 * @since 1.2.0
 */
@Ignore
public class JdbcPoolbsTest {

    @Test
    public void bsTest() {
        JdbcPoolBs jdbcPoolBs = JdbcPoolBs.newInstance()
                .username("root")
                .password("123456")
                .url("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");

        DataSource pooled = jdbcPoolBs.pooled();
        DataSource unPooled = jdbcPoolBs.unPooled();
    }

}
