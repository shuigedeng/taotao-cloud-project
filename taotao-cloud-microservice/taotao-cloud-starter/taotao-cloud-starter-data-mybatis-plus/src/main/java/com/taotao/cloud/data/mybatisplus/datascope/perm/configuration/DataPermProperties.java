package com.taotao.cloud.data.mybatisplus.datascope.perm.configuration;

import org.openjdk.nashorn.internal.objects.annotations.Getter;
import org.openjdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**   
* 数据权限配置
*/
@ConfigurationProperties("bootx.starter.data-perm")
public class DataPermProperties {

    /** 开启字段加密 */
    private boolean enableFieldDecrypt = true;

    /** 字段加密key 需要符合AES秘钥的要求 */
    private String fieldDecryptKey = "UCrtxSCwYZNCIlav";

    /** 开启数据权限 */
    private boolean enableDataPerm = true;

    /** 数据权限配置 */
    private DataPerm dataPerm = new DataPerm();

    /** 开启查询字段权限 */
    private boolean enableSelectFieldPerm = true;

	public boolean isEnableFieldDecrypt() {
		return enableFieldDecrypt;
	}

	public void setEnableFieldDecrypt(boolean enableFieldDecrypt) {
		this.enableFieldDecrypt = enableFieldDecrypt;
	}

	public String getFieldDecryptKey() {
		return fieldDecryptKey;
	}

	public void setFieldDecryptKey(String fieldDecryptKey) {
		this.fieldDecryptKey = fieldDecryptKey;
	}

	public boolean isEnableDataPerm() {
		return enableDataPerm;
	}

	public void setEnableDataPerm(boolean enableDataPerm) {
		this.enableDataPerm = enableDataPerm;
	}

	public DataPerm getDataPerm() {
		return dataPerm;
	}

	public void setDataPerm(DataPerm dataPerm) {
		this.dataPerm = dataPerm;
	}

	public boolean isEnableSelectFieldPerm() {
		return enableSelectFieldPerm;
	}

	public void setEnableSelectFieldPerm(boolean enableSelectFieldPerm) {
		this.enableSelectFieldPerm = enableSelectFieldPerm;
	}

	/**
     * 数据权限
     */
    public static class DataPerm {
        /** 表名 */
        private String table = "iam_user_dept";

        /** 查询字段(用户字段名) */
        private String queryField = "user_id";

        /** 条件字段(筛选条件字段名) */
        private String whereField = "dept_id";

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		public String getQueryField() {
			return queryField;
		}

		public void setQueryField(String queryField) {
			this.queryField = queryField;
		}

		public String getWhereField() {
			return whereField;
		}

		public void setWhereField(String whereField) {
			this.whereField = whereField;
		}
	}

}
