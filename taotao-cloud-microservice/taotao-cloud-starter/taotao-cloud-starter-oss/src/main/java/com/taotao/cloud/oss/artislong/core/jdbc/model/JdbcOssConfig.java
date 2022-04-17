package com.taotao.cloud.oss.artislong.core.jdbc.model;


import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import javax.sql.DataSource;

/**
 * @author 陈敏
 * @version JdbcOssConfig.java, v 1.0 2022/3/11 22:04 chenmin Exp $
 * Created on 2022/3/11
 */
public class JdbcOssConfig {

    private String basePath;

    private String dataSourceName;

    private String url;
    private Class<? extends DataSource> type;
    private String driver;
    private String username;
    private String password;

    public void init() {
        basePath = OssPathUtil.valid(basePath);
    }

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Class<? extends DataSource> getType() {
		return type;
	}

	public void setType(Class<? extends DataSource> type) {
		this.type = type;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
