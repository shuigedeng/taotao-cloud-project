package com.taotao.cloud.web.base.entity;

import com.taotao.cloud.common.utils.log.LogUtils;

import javax.annotation.PreDestroy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * 抽象侦听器
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-21 11:59:54
 */
public class JpaEntityListener {

	/**
	 * 在新实体持久化之前（添加到EntityManager）
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:54
	 */
	@PrePersist
	public void prePersist(Object object) {
		LogUtils.info(" AbstractListener prePersis: {}", object);

		// System.out.println("进行insert之前");
		// if(entity instanceof TestEntity) {
		// 	System.out.println(entity.toString());
		// 	CommonField commonField = ((TestEntity) entity).getCommonField();
		// 	if(ObjectUtils.isEmpty(commonField)) {
		// 		commonField=new CommonField();
		// 		commonField.setCreateTime(new Date());
		// 		commonField.setUpdateTime(new Date());
		// 		commonField.setCreateUserId("111");
		// 		commonField.setCreateUserName("ccc");
		// 		((TestEntity) entity).setCommonField(commonField);
		// 	}
		// 	System.out.println(entity.toString());
		// }
	}

	/**
	 * 在数据库中存储新实体（在commit或期间flush）
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:54
	 */
	@PostPersist
	public void postPersist(Object object) {
		LogUtils.info("AbstractListener postPersist: {}", object);
	}

	/**
	 * 从数据库中检索实体后。
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:55
	 */
	@PostLoad
	public void postLoad(Object object) {
		LogUtils.info("AbstractListener postLoad: {}", object);
	}

	/**
	 * 当一个实体被识别为被修改时EntityManager
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:54
	 */
	@PreUpdate
	public void preUpdate(Object object) {
		LogUtils.info("AbstractListener preUpdate: {}", object);
	}


	/**
	 * 更新数据库中的实体（在commit或期间flush）
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:54
	 */
	@PostUpdate
	public void postUpdate(Object object) {
		LogUtils.info("AbstractListener postUpdate: {}", object);
	}


	/**
	 * 在EntityManager中标记要删除的实体时
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:54
	 */
	@PreRemove
	public void preRemove(Object object) {
		LogUtils.info("AbstractListener preRemove: {}", object);
	}

	/**
	 * 从数据库中删除实体（在commit或期间flush）
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:55
	 */
	@PostRemove
	public void postRemove(Object object) {
		LogUtils.info("AbstractListener postRemove: {}", object);
	}

	/**
	 * 前摧毁
	 *
	 * @param object 对象
	 * @since 2022-10-21 11:59:54
	 */
	@PreDestroy
	public void preDestroy(Object object) {
		LogUtils.info("AbstractListener preDestroy: {}", object);
	}


}
