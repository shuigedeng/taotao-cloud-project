package com.taotao.cloud.data.analysis.datasource.ck;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClickHouseJdbcBaseDaoImpl {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(@Qualifier("clickHouseTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate =jdbcTemplate ;
    }

}
