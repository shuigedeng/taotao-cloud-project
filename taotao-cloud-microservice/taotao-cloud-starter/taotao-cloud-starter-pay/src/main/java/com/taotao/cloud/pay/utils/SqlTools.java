package com.taotao.cloud.pay.utils;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * jdbc sql生成语句生产
 */
public class SqlTools {

	public static final String SEPARATED = ", ";
	private static final Pattern pattern = Pattern.compile(":([0-9A-Za-z_]+)[" + SEPARATED + "]?");

	/**
	 * 获取统计的sql
	 *
	 * @param sql 原始sql
	 * @return 转化后的sql
	 */
	public static String getCountSQL(final String sql) {
		return getCountSQL(sql, null);
	}

	/**
	 * 获取统计的sql
	 *
	 * @param sql 原始sql
	 * @return 转化后的sql
	 */
	public static String getCountOracleSQL(final String sql) {
		return getCountOracleSQL(sql, null);

	}

	/**
	 * 获取统计的sql
	 *
	 * @param sql        原始sql
	 * @param countField 需要统计的字段
	 * @return 转化后的sql
	 */
	public static String getCountSQL(final String sql, String countField) {

		String countSql = String.format("SELECT  COUNT(%s) ",
			null == countField ? "*" : countField);
		String upperSql = sql.toUpperCase();
		int start = upperSql.indexOf("FROM ");
		int end = upperSql.lastIndexOf("ORDER BY ");
		countSql += sql.substring(start, end == -1 ? sql.length() : end);
		return countSql;
	}

	/**
	 * 获取统计的sql
	 *
	 * @param sql        原始sql
	 * @param countField 需要统计的字段
	 * @return 转化后的sql
	 */
	public static String getCountOracleSQL(final String sql, String countField) {

		String countSql = String.format("SELECT  COUNT(%s) ",
			null == countField ? "*" : countField);
		String upperSql = sql.toUpperCase().replace("SELECT * FROM ( ", "")
			.replace(" ) WHERE ROWNUM BETWEEN %s AND %s", "");
		int start = upperSql.indexOf("FROM ");
		int end = upperSql.lastIndexOf("ORDER BY ");
		countSql += sql.substring(start, end == -1 ? sql.length() : end);
		return countSql;
	}

	/**
	 * 移除sql的group部分
	 *
	 * @param sql 原始sql
	 * @return 转化后的sql
	 */
	public static String removeGROUP(final String sql) {

		String upperSql = sql.toUpperCase();
		int end = upperSql.indexOf(" GROUP ");
		return -1 == end ? sql : sql.substring(0, end);
	}

	/**
	 * 获取sql
	 *
	 * @param select    需要查询的字段
	 * @param tableName 表名
	 * @return sql
	 */
	public static String getSelectSQL(String select, String tableName) {
		if (null == select || "".equals(select)) {
			select = "*";
		}
		return String.format("select %s from %s ", select, tableName);

	}


	/**
	 * 获取sql
	 *
	 * @param select    需要查询的字段
	 * @param tableName 表名
	 * @param alias     别名
	 * @param where     条件开始的语句
	 * @return 拼装好的sql
	 */
	public static String getSQL(String select, String tableName, String alias, String where) {
		return String.format("%s %s %s", getSelectSQL(select, tableName), alias, where);
	}


	/**
	 * 拼接分页部分
	 *
	 * @param pageNumber 页号
	 * @param pageSize   每页大小
	 * @return 拼装 分页sql
	 */
	public static String forPaginate(int pageNumber, int pageSize) {
		int offset = pageSize * (pageNumber - 1);
		return String.format(" limit %s,%s", offset, pageSize);
	}


	/**
	 * 拼接分页部分
	 *
	 * @param sql        sql
	 * @param pageNumber 页号
	 * @param pageSize   每页大小
	 * @return 拼装 分页sql
	 */
	public static String forPaginate(String sql, int pageNumber, int pageSize) {
		int offset = pageSize * (pageNumber - 1);
		String sqltmp = String.format(
			"SELECT *  FROM (SELECT ROWNUM  RN,a.* FROM (  %s  ) a  WHERE ROWNUM <= %s) WHERE RN >%s",
			sql, offset + pageSize, offset);
		return sqltmp;
	}


	/**
	 * 设置 参数
	 *
	 * @param ps     代替对象
	 * @param params 参数
	 * @throws SQLException 设置参数异常
	 */
	public static void fillStatement(PreparedStatement ps, List<Object> params)
		throws SQLException {
		if (null == params || params.isEmpty()) {
			return;
		}

		int i = 0;
		for (Object param : params) {
			if (param instanceof List) {
				fillStatement(ps, (List) param);
				ps.addBatch();
				continue;
			}

			if (param instanceof Object[]) {
				fillStatement(ps, (Object[]) param);
				ps.addBatch();
				continue;
			}

			if (null == param) {
				ps.setNull(++i, Types.OTHER);
				continue;
			}
			ps.setObject(++i, param);

		}

	}

	/**
	 * 设置 参数
	 *
	 * @param ps     代替对象
	 * @param params 参数
	 * @throws SQLException 设置参数异常
	 */
	public static void fillStatement(PreparedStatement ps, Object... params) throws SQLException {

		if (null == params) {
			return;
		}
		if (params instanceof Object[][]) {
			for (Object[] v : (Object[][]) params) {
				fillStatement(ps, v);
				ps.addBatch();
			}
		} else {
			int i = 0;
			for (Object param : params) {
				if (null == param) {
					ps.setNull(++i, Types.OTHER);
					continue;
				}
				ps.setObject(++i, param);
			}
		}


	}


	/**
	 * 生成插入语句
	 *
	 * @param tableName      表名
	 * @param keyColumnNames 数据库列集
	 * @return 插入语句
	 */
	public static StringBuilder generateInsertString(String tableName,
		List<String> keyColumnNames) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(tableName).append("(");
		StringBuilder temp = new StringBuilder(") values(");
		boolean flag = false;
		for (String colName : keyColumnNames) {
			if (flag) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("").append(colName).append("");
			temp.append("?");
			flag = true;
		}
		return sql.append(temp.toString()).append(")");

	}

	/**
	 * 生成更新语句
	 *
	 * @param tableName      表名
	 * @param keyColumnNames 数据库列集
	 * @param idColumn       主键列
	 * @return 更新语句
	 */
	public static StringBuilder generateUpdateByRowIdString(String tableName,
		List<String> keyColumnNames, String idColumn) {
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(tableName).append(" set ");
		int i = 0;
		for (String colName : keyColumnNames) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("").append(colName).append(" = ? ");
			i++;
		}
//		keyColumnNames.add(idColumn);
		sql.append(" where ").append(idColumn).append(" = ?");
		return sql;
	}

	/**
	 * 通过 Map 获取保存的sql
	 *
	 * @param table 表名
	 * @param attrs Map属性集
	 * @param paras 参数集
	 * @return 生成sql
	 */
	public static StringBuilder forMapSave(String table, Map<String, Object> attrs,
		List<Object> paras) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(table).append("(");
		StringBuilder temp = new StringBuilder(") values(");
		for (Map.Entry<String, Object> entry : attrs.entrySet()) {
			if (null == entry.getValue()) {
				continue;
			}
			if (paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append(' ').append(entry.getKey()).append(' ');
			temp.append("?");
			paras.add(entry.getValue());
		}
		sql.append(temp).append(")");
		return sql;
	}


	/**
	 * 生成更新的sql
	 *
	 * @param table    表名
	 * @param attrs    更新的属性
	 * @param where    条件部分
	 * @param whereVal 条件对应的值
	 * @param paras    参数集
	 * @return 更新的sql
	 */
	public static StringBuilder forMapUpdate(String table, Map<String, Object> attrs, String where,
		List<Object> whereVal, List<Object> paras) {

		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(table).append(" set ");
		for (Map.Entry<String, Object> entry : attrs.entrySet()) {
			if (!paras.isEmpty()) {
				sql.append(", ");
			}
			sql.append(' ').append(entry.getKey()).append(" = ? ");
			paras.add(entry.getValue());
		}

		sql.append(where);
		paras.addAll(whereVal);

		return sql;
	}

	/**
	 * 生成更新的sql
	 *
	 * @param table 表名
	 * @param attrs 更新的属性
	 * @param where 条件部分 and eq 级别
	 * @param paras 参数集
	 * @return 更新的sql
	 */
	public static StringBuilder forMapUpdate(String table, Map<String, Object> attrs,
		Map<String, Object> where, List<Object> paras) {
		boolean flag = false;
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(table).append(" set ");
		for (Map.Entry<String, Object> entry : attrs.entrySet()) {
			if (flag) {
				sql.append(", ");
			}
			sql.append(' ').append(entry.getKey()).append(" = ? ");
			paras.add(entry.getValue());
			flag = true;
		}
		sql.append(" where ");
		for (Map.Entry<String, Object> entry : where.entrySet()) {
			if (!flag) {
				sql.append(" and ");
			}
			sql.append(' ').append(entry.getKey()).append(" = ? ");
			paras.add(entry.getValue());
			flag = false;
		}
		return sql;
	}


	/**
	 * 冒号形式的sql转化为问号形式
	 *
	 * @param sql    原始sql
	 * @param attrs  属性
	 * @param values 转化后的属性集
	 * @return 转化后的sql
	 */
	public static String forConverSQL(String sql, Map<String, Object> attrs, List<Object> values) {
		Matcher matcher = pattern.matcher(sql);
		String rexp = null;
		while (matcher.find()) {
			String group = matcher.group(1);
			Object ov = attrs.get(group);
			if (ov instanceof Collection) {
				StringBuilder sb = new StringBuilder();
				Collection vs = (Collection) ov;
				for (Object v : vs) {
					sb.append("?,");
					values.add(v);
				}
				sb.deleteCharAt(sb.length() - 1);
				rexp = sb.toString();
			} else {
				values.add(ov);
				rexp = "?";
			}
			sql = sql.replace(String.format(":%s", group), rexp);
		}
		return sql;
	}

	/**
	 * 根据数组对象生成对应的in部分
	 *
	 * @param num 生成"?"的数量
	 *
	 *            <code> forQuestionMarkSQL(3)  = ?,?,? </code>
	 * @return 问号，逗号隔开
	 */
	public static String forQuestionMarkSQL(int num) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/**
	 * 将集合以分隔符的形式进行连接成对应字符串
	 *
	 * @param list      集合
	 * @param separated 分隔符
	 * @return 连接字符串
	 */
	public static String join(List<String> list, String separated) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (sb.length() != 0) {
				sb.append(separated);
			}
			sb.append(list.get(i));

		}
		return sb.toString();

	}


}
