//package com.taotao.cloud.goods.biz.task;
//
//import com.taotao.cloud.cache.redis.repository.RedisRepository;
//import com.taotao.cloud.common.enums.CachePrefix;
//import com.taotao.cloud.job.xxl.timetask.EveryDayExecute;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 热点词汇每天任务执行
// *
// * @author shuigedeng
// * @version 2022.04
// * @since 2022-04-27 16:54:14
// */
//@Component
//public class HotWordsEveryDayTaskExecute implements EveryDayExecute {
//
//	/**
//	 * 复述,库
//	 */
//	@Autowired
//	private RedisRepository redisRepository;
//
//	/**
//	 * 执行每日任务
//	 */
//	@Override
//	public void execute() {
//		//移除昨日的热搜词
//		redisRepository.del(CachePrefix.HOT_WORD.getPrefix());
//	}
//
//}
