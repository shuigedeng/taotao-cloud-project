package com.taotao.cloud.data.analysis.datasource.tidb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TidbJdbcBaseDaoImpl {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(@Qualifier("tidbTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate =jdbcTemplate ;
    }

}
