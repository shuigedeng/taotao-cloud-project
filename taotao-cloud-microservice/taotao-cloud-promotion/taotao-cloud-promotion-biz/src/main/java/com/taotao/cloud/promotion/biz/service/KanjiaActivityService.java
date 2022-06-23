package com.taotao.cloud.promotion.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.promotion.api.web.query.KanjiaActivityPageQuery;
import com.taotao.cloud.promotion.api.web.vo.kanjia.KanjiaActivitySearchQuery;
import com.taotao.cloud.promotion.api.web.vo.kanjia.KanjiaActivityVO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;


/**
 * 砍价活动参与记录业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:44
 */
public interface KanjiaActivityService extends IService<KanjiaActivity> {

	/**
	 * 获取砍价活动
	 *
	 * @param kanJiaActivitySearchParams 砍价活动搜索参数
	 * @return {@link KanjiaActivity }
	 * @since 2022-04-27 16:43:44
	 */
	KanjiaActivity getKanjiaActivity(KanjiaActivitySearchQuery kanJiaActivitySearchParams);

	/**
	 * 获取砍价活动
	 * <p>
	 * 有值说明是已参加的砍价活动
	 * 没有值说明是未参加的砍价活动
	 *
	 * @param kanJiaActivitySearchParams 砍价活动搜索参数
	 * @return {@link KanjiaActivityVO }
	 * @since 2022-04-27 16:43:44
	 */
	KanjiaActivityVO getKanjiaActivityVO(KanjiaActivitySearchQuery kanJiaActivitySearchParams);

	/**
	 * 发起人发起砍价活动
	 *
	 * @param id 活动ID
	 * @return {@link KanjiaActivityLog }
	 * @since 2022-04-27 16:43:44
	 */
	KanjiaActivityLog add(String id);

	/**
	 * 帮砍
	 *
	 * @param kanJiaActivityId 活动id
	 * @return {@link KanjiaActivityLog }
	 * @since 2022-04-27 16:43:44
	 */
	KanjiaActivityLog helpKanJia(String kanJiaActivityId);

	/**
	 * 根据条件查询我参与的砍价活动
	 *
	 * @param kanJiaActivityPageQuery 砍价活动查询条件
	 * @param page                分页对象
	 * @return {@link IPage }<{@link KanjiaActivity }>
	 * @since 2022-04-27 16:43:45
	 */
	IPage<KanjiaActivity> getForPage(KanjiaActivityPageQuery kanJiaActivityPageQuery, PageParam page);


}
