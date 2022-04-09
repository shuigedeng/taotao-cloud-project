package com.taotao.cloud.data.jpa.bean;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.taotao.cloud.common.utils.common.IdGeneratorUtil;
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
		
		// 采用雪花算法获取id,时间回拨会存在重复,这里用随机数来减少重复的概率
		//final Snowflake snowflake = IdUtil.getSnowflake(1, (int) (Math.random() * 20 + 1));
		//return snowflake.nextId();

		return IdGeneratorUtil.getId();
	}

}
