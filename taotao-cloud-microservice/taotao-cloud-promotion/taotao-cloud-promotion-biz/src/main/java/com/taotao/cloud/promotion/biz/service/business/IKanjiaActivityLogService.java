package com.taotao.cloud.promotion.biz.service.business;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityDTO;
import com.taotao.cloud.promotion.api.model.query.KanJiaActivityLogPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;


/**
 * 砍价活动日志业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:40
 */
public interface IKanjiaActivityLogService extends IService<KanjiaActivityLog> {

	/**
	 * 根据砍价参与记录id查询砍价记录
	 *
	 * @param kanJiaActivityLogPageQuery 砍价活动帮砍信息
	 * @param pageVO                 分页信息
	 * @return {@link IPage }<{@link KanjiaActivityLog }>
	 * @since 2022-04-27 16:43:40
	 */
	IPage<KanjiaActivityLog> getForPage(KanJiaActivityLogPageQuery kanJiaActivityLogPageQuery, PageQuery pageVO);

	/**
	 * 砍一刀
	 *
	 * @param kanJiaActivityDTO 砍价记录
	 * @return {@link KanjiaActivityLog }
	 * @since 2022-04-27 16:43:40
	 */
	KanjiaActivityLog addKanJiaActivityLog(KanjiaActivityDTO kanJiaActivityDTO);
}
