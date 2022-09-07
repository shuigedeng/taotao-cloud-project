package com.taotao.cloud.data.mybatis.plus.datascope.dataPermission.dept.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.data.mybatis.plus.datascope.dataPermission.dept.service.DeptDataPermissionFrameworkService;
import com.taotao.cloud.data.mybatis.plus.datascope.dataPermission.rule.DataPermissionRule;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于部门的 {@link DataPermissionRule} 数据权限规则实现 注意，使用 DeptDataPermissionRule 时，需要保证表中有
 * dept_id 部门编号的字段，可自定义。 根据登录用户拥有的数据权限构建查询条件
 */
public class DeptDataPermissionRule implements DataPermissionRule {

	private static final String DEPT_COLUMN_NAME = "dept_id";

	private static final String USER_COLUMN_NAME = "user_id";

	static final Expression EXPRESSION_NULL = new NullValue();

	private final DeptDataPermissionFrameworkService deptDataPermissionService;

	/**
	 * 基于部门的表字段配置 一般情况下，每个表的部门编号字段是 dept_id，通过该配置自定义。
	 * <p>
	 * key：表名 value：字段名
	 */
	private final Map<String, String> deptColumns = new HashMap<>();

	/**
	 * 基于用户的表字段配置 一般情况下，每个表的部门编号字段是 dept_id，通过该配置自定义。
	 * <p>
	 * key：表名 value：字段名
	 */
	private final Map<String, String> userColumns = new HashMap<>();

	/**
	 * 所有表名，是 {@link #deptColumns} 和 {@link #userColumns} 的合集
	 */
	private final Set<String> TABLE_NAMES = new HashSet<>();

	public DeptDataPermissionRule(DeptDataPermissionFrameworkService deptDataPermissionService) {
		this.deptDataPermissionService = deptDataPermissionService;
	}

	/**
	 * 获取配置此规则的所有表名
	 */
	@Override
	public Set<String> getTableNames() {
		return TABLE_NAMES;
	}

	/**
	 * 根据表名以及登录用户的数据权限构建查询条件
	 * @param tableName 表名
	 * @param tableAlias 别名，可能为空
	 * @return 查询条件
	 */
	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		// 只有有登陆用户的情况下，才进行数据权限的处理
		SecurityUser loginUser = SecurityUtils.getCurrentUserWithNull();
		if (loginUser == null) {
			return null;
		}

		loginUser.

		// 获得用户的数据权限
		DeptDataPermissionRespDTO deptDataPermission = deptDataPermissionService.getDeptDataPermission(loginUser);
		if (deptDataPermission == null) {
			log.error("[getExpression][LoginUser({}) 获取数据权限为 null]", loginUser);
			throw new NullPointerException(String.format("LoginUser(%d) Table(%s/%s) 未返回数据权限", loginUser.getUserId(),
					tableName, tableAlias.getName()));
		}

		// 情况一，如果是 ALL 可查看全部，则无需拼接条件
		if (deptDataPermission.getAll()) {
			return null;
		}

		// 情况二，即不能查看部门，又不能查看自己，则说明 100% 无权限
		if (CollUtil.isEmpty(deptDataPermission.getDeptIds()) && Boolean.FALSE.equals(deptDataPermission.getSelf())) {
			// WHERE null = null，可以保证返回的数据为空
			return new EqualsTo(null, null);
		}

		// 情况三，拼接 Dept 和 User 的条件，最后组合
		Expression deptExpression = this.buildDeptExpression(tableName, tableAlias, deptDataPermission.getDeptIds());
		Expression userExpression = this.buildUserExpression(tableName, tableAlias, deptDataPermission.getSelf(),
				loginUser.getUserId());

		if (deptExpression == null && userExpression == null) {
			log.warn("[getExpression][LoginUser({}) Table({}/{}) DeptDataPermission({}) 构建的条件为空]", loginUser, tableName,
					tableAlias, deptDataPermission);
			return EXPRESSION_NULL;
		}
		if (deptExpression == null) {
			return userExpression;
		}
		if (userExpression == null) {
			return deptExpression;
		}

		// 目前，如果有指定部门 + 可查看自己，采用 OR 条件。即，WHERE dept_id IN ? OR user_id = ?
		return new OrExpression(deptExpression, userExpression);
	}

	/**
	 * 构建部门查询的条件
	 * @param tableName 表名
	 * @param tableAlias 表别名
	 * @param deptIds 允许查看的部门id
	 * @return 查询条件 where dept_id in ()
	 */
	private Expression buildDeptExpression(String tableName, Alias tableAlias, Set<Long> deptIds) {
		// 如果不存在这张表的配置，则无需作为条件
		String columnName = deptColumns.get(tableName);
		if (StrUtil.isEmpty(columnName)) {
			return null;
		}

		// 如果允许查看的部门为空，则不拼接部门查询条件
		if (CollUtil.isEmpty(deptIds)) {
			return null;
		}

		// 拼接条件
		return new InExpression(MyBatisUtils.buildColumn(tableName, tableAlias, columnName),
				new ExpressionList(deptIds.stream().map(LongValue::new).collect(Collectors.toList())));
	}

	/**
	 * 构建用户查询的条件
	 * @param tableName 表名
	 * @param tableAlias 表别名
	 * @param self 是否只允许自己查看
	 * @param userId 用户id
	 * @return 查询条件 where user_id = ''
	 */
	private Expression buildUserExpression(String tableName, Alias tableAlias, Boolean self, Long userId) {
		// 如果不查看自己，则无需作为条件
		if (Boolean.FALSE.equals(self)) {
			return null;
		}

		// 获取这张表中作为查询条件的列名
		String columnName = userColumns.get(tableName);
		if (StrUtil.isEmpty(columnName)) {
			return null;
		}

		// 拼接条件
		return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, columnName), new LongValue(userId));
	}

	/**
	 * entityClass对应的表 以DEPT_COLUMN_NAME为查询条件
	 */
	public void addDeptColumn(Class<? extends BaseEntity> entityClass) {
		addDeptColumn(entityClass, DEPT_COLUMN_NAME);
	}

	/**
	 * entityClass对应的表 以columnName列为查询条件
	 */
	public void addDeptColumn(Class<? extends BaseEntity> entityClass, String columnName) {
		String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
		addDeptColumn(tableName, columnName);
	}

	/**
	 * 储存使用此规则的表名、列名
	 */
	public void addDeptColumn(String tableName, String columnName) {
		deptColumns.put(tableName, columnName);
		TABLE_NAMES.add(tableName);
	}

	/**
	 * entityClass对应的实体类以USER_COLUMN_NAME列来查询
	 */
	public void addUserColumn(Class<? extends BaseEntity> entityClass) {
		addUserColumn(entityClass, USER_COLUMN_NAME);
	}

	/**
	 * entityClass对应的表 以columnName列为查询条件
	 */
	public void addUserColumn(Class<? extends BaseEntity> entityClass, String columnName) {
		String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
		addUserColumn(tableName, columnName);
	}

	/**
	 * 储存使用此规则的表名、列名
	 */
	public void addUserColumn(String tableName, String columnName) {
		userColumns.put(tableName, columnName);
		TABLE_NAMES.add(tableName);
	}

}
