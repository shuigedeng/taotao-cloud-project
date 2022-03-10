package com.taotao.cloud.data.jpa.bean;

import com.taotao.cloud.common.utils.IdGeneratorUtil;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 雪花算法ID生成器
 */
public class SnowFlakeIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object o)
		throws HibernateException {
		return IdGeneratorUtil.getId();
	}

}
