package com.taotao.cloud.sys.biz.springboot.mysql.service.impl;

import com.hrhx.springboot.domain.FundPublic;
import com.hrhx.springboot.mysql.service.FundPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * @author duhongming
 *
 */
@Service
public class FundPublicServiceImpl implements FundPublicService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<FundPublic> getFundPublic() {
        String sql = "SELECT p.fund_code,p.fund_name FROM fund_public p LEFT JOIN(\n" +
                "SELECT fund_code,fund_name FROM fund_info GROUP BY fund_code,fund_name) i\n" +
                "ON p.fund_code=i.fund_code WHERE i.fund_code is NULL;";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FundPublic.class));
    }
}
