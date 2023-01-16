package com.taotao.cloud.data.analysis.hive.service;

import com.taotao.cloud.data.analysis.datasource.hive.HiveJdbcBaseDaoImpl;
import com.taotao.cloud.data.analysis.hive.model.HiveUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HiveUserService extends HiveJdbcBaseDaoImpl {

	public void testUseJdbcTemplate(){
		getJdbcTemplate().query("select * from hive_user", rs -> {
			System.out.println(rs);
		});
	}
}
