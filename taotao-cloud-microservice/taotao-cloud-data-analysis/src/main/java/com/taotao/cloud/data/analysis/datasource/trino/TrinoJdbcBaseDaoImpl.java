package com.taotao.cloud.data.analysis.datasource.trino;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TrinoJdbcBaseDaoImpl {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(@Qualifier("trinoTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate =jdbcTemplate ;
    }

}
