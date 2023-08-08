package com.taotao.cloud.sys.biz.mbg;

import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.taotao.cloud.sys.biz.mbg.AppDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AppService {

    @Resource
    private AppMapper sysUserMapper;

    public List<App> getSysUserList(String account1) {
        SelectStatementProvider select = SqlBuilder.select(app.allColumns())
                .from(app)
                .where(name, isLike(account1).filter(Objects::isNull).map(s -> "%" + s + "%"))
                .and(version, isEqualTo(0))
                .and(version, isEqualTo(0))
                .orderBy(createTime.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return sysUserMapper.selectMany(select);
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean insert(String account, String name) {
		App sysUser = new App();
        sysUser.setDelFlag(true);
        return sysUserMapper.insert(sysUser) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String account1) {
        DeleteStatementProvider del = deleteFrom(app)
                .where(name, isEqualTo(account1))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        sysUserMapper.delete(del);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(String account1, String nickName1, Integer state1) {
        UpdateStatementProvider update = SqlBuilder.update(app)
                .set(name).equalTo(nickName1)
                .set(version).equalTo(state1)
                .set(updateTime).equalTo(LocalDateTime.now())
                .where(name, isEqualTo(account1))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return sysUserMapper.update(update) > 0;
    }

//    public PageBean<App> getSysUsersByPage(String account1, Integer pageIndex, Integer pageSize) {
//        PageHelper.startPage(pageIndex, pageSize);
//        return new PageBean<App>(sysUserMapper.selectMany(select(app.allColumns())
//                .from(app)
//                .where(name, isLike(account1).filter(Objects::isNull).map(x -> "%" + x + "%"))
//                .and(version, isEqualTo(0))
//                .orderBy(updateTime.descending())
//                .build()
//                .render(RenderingStrategies.MYBATIS3)));
//
//    }

    public long getCount() {
        SelectStatementProvider selectStatement = select(count(app.id))
                .from(app)
                .where(version, isEqualTo(0))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return sysUserMapper.count(selectStatement);
    }

//关联查询
//直接在java中使用实体对象来关联
//	public List<App> getMenuList(String account1) {
//		return sysMenuMapper.selectMany(select(app.allColumns())
//			.from(sysMenu, "m")
//			.leftJoin(sysRoleMenu, "mr").on(sysMenu.name, equalTo(sysRoleMenu.menuName))
//			.leftJoin(sysUserRole, "ur").on(sysRoleMenu.roleName, equalTo(sysUserRole.roleName))
//			.leftJoin(sysUser, "u").on(sysUserRole.account, equalTo(sysUser.account))
//			.where(sysUser.account, isEqualTo(account1))
//			.and(sysUser.state, isEqualTo(0))
//			.build()
//			.render(RenderingStrategies.MYBATIS3));
//	}


	/**
	 * <pre>
	 *     select user.id, order_item.goods_name, sum(order_item.goods_quantity) from user
	 *      inner join `order` on user.id = `order`.user_id
	 *      inner join order_item on `order`.id = order_item.order_id
	 *      where user.id = 1 group by order_item.goods_name
	 * </pre>
	 *
	 * @param userId
	 * @return
	 */
//	public List<UserPurchasedGoods> selectUserPurchasedOrders(Long userId) {
//		// alias
//		val user = UserEntityDynamicSqlSupport.userEntity;
//		val orderItem = OrderItemEntityDynamicSqlSupport.orderItemEntity;
//		val order = OrderEntityDynamicSqlSupport.orderEntity;
//		// build sql dsl
//		val sqlDsl = select(user.id, orderItem.goodsName, sum(orderItem.goodsQuantity).as("goods_quantity"))
//			.from(user)
//			.join(order).on(user.id, equalTo(order.userId))
//			.join(orderItem).on(order.id, equalTo(orderItem.orderId))
//			.where(user.id, isEqualTo(userId))
//			.groupBy(orderItem.goodsName)
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		// execute and map
//		return userEntityMapper.selectMany(sqlDsl, map -> {
//			val result = new UserPurchasedGoods();
//			result.setGoodsQuantity(Integer.parseInt(map.get("goods_quantity").toString()));
//			result.setGoodsName(map.get("goods_name").toString());
//			return result;
//		});
//	}

	//分页查询
//	public List<UmsAdmin> list(Integer pageNum, Integer pageSize, String username, List<Integer> statusList) {
//		PageHelper.startPage(pageNum, pageSize);
//		SelectStatementProvider selectStatement = SqlBuilder.select(UmsAdminMapper.selectList)
//			.from(UmsAdminDynamicSqlSupport.umsAdmin)
//			.where(UmsAdminDynamicSqlSupport.username, isEqualToWhenPresent(username))
//			.and(UmsAdminDynamicSqlSupport.status, isIn(statusList))
//			.orderBy(UmsAdminDynamicSqlSupport.createTime.descending())
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		return adminMapper.selectMany(selectStatement);
//	}

	//lambda查询
//	public List<UmsAdmin> lambdaList(Integer pageNum, Integer pageSize, String username, List<Integer> statusList) {
//		PageHelper.startPage(pageNum, pageSize);
//		List<UmsAdmin> list = adminMapper.select(c -> c.where(UmsAdminDynamicSqlSupport.username, isEqualToWhenPresent(username))
//			.and(UmsAdminDynamicSqlSupport.status, isIn(statusList))
//			.orderBy(UmsAdminDynamicSqlSupport.createTime.descending()));
//		return list;
//	}

	//子查询
//	public List<UmsAdmin> subList(Long roleId) {
//		SelectStatementProvider selectStatement = SqlBuilder.select(UmsAdminMapper.selectList)
//			.from(UmsAdminDynamicSqlSupport.umsAdmin)
//			.where(UmsAdminDynamicSqlSupport.id, isIn(SqlBuilder.select(UmsAdminRoleRelationDynamicSqlSupport.adminId)
//				.from(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
//				.where(UmsAdminRoleRelationDynamicSqlSupport.roleId, isEqualTo(roleId))))
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		return adminMapper.selectMany(selectStatement);
//	}

	//group join查询
//	public List<RoleStatDto> groupList() {
//		SelectStatementProvider selectStatement = SqlBuilder.select(UmsRoleDynamicSqlSupport.id.as("roleId"), UmsRoleDynamicSqlSupport.name.as("roleName"), count(UmsAdminDynamicSqlSupport.id).as("count"))
//			.from(UmsRoleDynamicSqlSupport.umsRole)
//			.leftJoin(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
//			.on(UmsRoleDynamicSqlSupport.id, equalTo(UmsAdminRoleRelationDynamicSqlSupport.roleId))
//			.leftJoin(UmsAdminDynamicSqlSupport.umsAdmin)
//			.on(UmsAdminRoleRelationDynamicSqlSupport.adminId, equalTo(UmsAdminDynamicSqlSupport.id))
//			.groupBy(UmsRoleDynamicSqlSupport.id)
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		return adminDao.groupList(selectStatement);
//	}

//
//	public void deleteByUsername(String username) {
//		DeleteStatementProvider deleteStatement = SqlBuilder.deleteFrom(UmsAdminDynamicSqlSupport.umsAdmin)
//			.where(UmsAdminDynamicSqlSupport.username, isEqualTo(username))
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		adminMapper.delete(deleteStatement);
//	}
//
//	public void updateByIds(List<Long> ids, Integer status) {
//		UpdateStatementProvider updateStatement = SqlBuilder.update(UmsAdminDynamicSqlSupport.umsAdmin)
//			.set(UmsAdminDynamicSqlSupport.status).equalTo(status)
//			.where(UmsAdminDynamicSqlSupport.id, isIn(ids))
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		adminMapper.update(updateStatement);
//	}
//
//	public AdminRoleDto selectWithRoleList(Long id) {
//		List<BasicColumn> columnList = new ArrayList<>(CollUtil.toList(UmsAdminMapper.selectList));
//		columnList.add(UmsRoleDynamicSqlSupport.id.as("role_id"));
//		columnList.add(UmsRoleDynamicSqlSupport.name.as("role_name"));
//		columnList.add(UmsRoleDynamicSqlSupport.description.as("role_description"));
//		columnList.add(UmsRoleDynamicSqlSupport.createTime.as("role_create_time"));
//		columnList.add(UmsRoleDynamicSqlSupport.status.as("role_status"));
//		columnList.add(UmsRoleDynamicSqlSupport.sort.as("role_sort"));
//		SelectStatementProvider selectStatement = SqlBuilder.select(columnList)
//			.from(UmsAdminDynamicSqlSupport.umsAdmin)
//			.leftJoin(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
//			.on(UmsAdminDynamicSqlSupport.id, equalTo(UmsAdminRoleRelationDynamicSqlSupport.adminId))
//			.leftJoin(UmsRoleDynamicSqlSupport.umsRole)
//			.on(UmsAdminRoleRelationDynamicSqlSupport.roleId, equalTo(UmsRoleDynamicSqlSupport.id))
//			.where(UmsAdminDynamicSqlSupport.id, isEqualTo(id))
//			.build()
//			.render(RenderingStrategies.MYBATIS3);
//		return adminDao.selectWithRoleList(selectStatement);
//	}
}

