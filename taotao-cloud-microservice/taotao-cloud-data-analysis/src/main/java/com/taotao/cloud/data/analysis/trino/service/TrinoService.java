package com.taotao.cloud.data.analysis.trino.service;

import com.taotao.cloud.data.analysis.datasource.trino.TrinoJdbcBaseDaoImpl;
import com.taotao.cloud.data.analysis.trino.model.TrinoUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrinoService extends TrinoJdbcBaseDaoImpl {

	public void testUseJdbcTemplate(){
		getJdbcTemplate().query("select * from trino_user", rs -> {
			System.out.println(rs);
		});
	}
}
