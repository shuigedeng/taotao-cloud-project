package com.taotao.cloud.workflow.api.database.model.interfaces;

import java.sql.SQLException;
import lombok.Data;

@Data
public abstract class JdbcGetMod {

	/**
	 * 设置自定义模板接口
	 *
	 * @param modelDTO 模板相关参数
	 * @throws SQLException ignore
	 */
	public abstract void setMod(ModelDTO modelDTO) throws SQLException;

}
