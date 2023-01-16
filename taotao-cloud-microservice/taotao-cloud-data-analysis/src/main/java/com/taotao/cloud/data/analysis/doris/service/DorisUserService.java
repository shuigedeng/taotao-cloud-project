package com.taotao.cloud.data.analysis.doris.service;

import com.taotao.cloud.data.analysis.clickhouse.mapper.CkUserMapper;
import com.taotao.cloud.data.analysis.clickhouse.model.CkUser;
import com.taotao.cloud.data.analysis.datasource.ck.ClickHouseJdbcBaseDaoImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DorisUserService extends ClickHouseJdbcBaseDaoImpl {

	@Autowired
	private CkUserMapper userMapper;

	public void testUseJdbcTemplate(){
		getJdbcTemplate().query("select * from user", rs -> {
			System.out.println(rs);
		});
	}

	public List testUseMapperInterface(){
		List userList = userMapper.queryUser();

		CkUser user = new CkUser();
		Integer flag = userMapper.insertUser(user);

		System.out.println("dslfkajsldflsdfk");
		return userList;
	}
}
