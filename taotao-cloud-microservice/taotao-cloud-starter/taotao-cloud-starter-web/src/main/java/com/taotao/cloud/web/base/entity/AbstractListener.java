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
 * @version 2022.04
 * @since 2022-05-24 09:22:40
 */
public class AbstractListener {

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

	@PreUpdate
	public void preUpdate(Object object) {
		LogUtils.info("AbstractListener preUpdate: {}", object);
	}

	@PreRemove
	public void preRemove(Object object) {
		LogUtils.info("AbstractListener preRemove: {}", object);
	}

	@PreDestroy
	public void preDestroy(Object object) {
		LogUtils.info("AbstractListener preDestroy: {}", object);
	}

	@PostPersist
	public void postPersist(Object object) {
		LogUtils.info("AbstractListener postPersist: {}", object);
	}

	@PostUpdate
	public void postUpdate(Object object) {
		LogUtils.info("AbstractListener postUpdate: {}", object);
	}

	@PostRemove
	public void postRemove(Object object) {
		LogUtils.info("AbstractListener postRemove: {}", object);
	}

	@PostLoad
	public void postLoad(Object object) {
		LogUtils.info("AbstractListener postLoad: {}", object);
	}

}
