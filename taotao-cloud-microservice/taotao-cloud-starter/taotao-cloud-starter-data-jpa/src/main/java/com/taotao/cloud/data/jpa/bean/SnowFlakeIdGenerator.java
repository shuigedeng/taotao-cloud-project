package com.taotao.cloud.data.jpa.bean;

import com.taotao.cloud.common.utils.common.IdGeneratorUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 雪花算法ID生成器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:16:48
 */
public class SnowFlakeIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object o)
		throws HibernateException {
		
		// 采用雪花算法获取id,时间回拨会存在重复,这里用随机数来减少重复的概率
		//final Snowflake snowflake = IdUtil.getSnowflake(1, (int) (Math.random() * 20 + 1));
		//return snowflake.nextId();

		return IdGeneratorUtil.getId();
	}

}
