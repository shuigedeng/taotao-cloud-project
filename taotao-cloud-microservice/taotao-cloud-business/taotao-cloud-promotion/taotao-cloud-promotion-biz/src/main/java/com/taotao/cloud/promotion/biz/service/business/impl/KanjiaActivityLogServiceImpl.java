/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.promotion.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityDTO;
import com.taotao.cloud.promotion.api.model.page.KanJiaActivityLogPageQuery;
import com.taotao.cloud.promotion.biz.mapper.KanJiaActivityLogMapper;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityLogService;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityService;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 砍价活动日志业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KanjiaActivityLogServiceImpl extends ServiceImpl<KanJiaActivityLogMapper, KanjiaActivityLog>
	implements IKanjiaActivityLogService {

	@Autowired
	private IKanjiaActivityGoodsService kanJiaActivityGoodsService;

	@Autowired
	private IKanjiaActivityService kanJiaActivityService;

	@Override
	public IPage<KanjiaActivityLog> getForPage(KanJiaActivityLogPageQuery kanJiaActivityLogPageQuery, PageVO pageVO) {
		QueryWrapper<KanjiaActivityLog> queryWrapper = kanJiaActivityLogPageQuery.wrapper();
		return this.page(PageUtil.initPage(pageVO), queryWrapper);
	}

	@Override
	public KanjiaActivityLog addKanJiaActivityLog(KanjiaActivityDTO kanjiaActivityDTO) {
		// 校验当前会员是否已经参与过此次砍价
		LambdaQueryWrapper<KanjiaActivityLog> queryWrapper = new LambdaQueryWrapper<KanjiaActivityLog>();
		queryWrapper.eq(
			kanjiaActivityDTO.getKanjiaActivityId() != null,
			KanjiaActivityLog::getKanjiaActivityId,
			kanjiaActivityDTO.getKanjiaActivityId());
		queryWrapper.eq(
			KanjiaActivityLog::getKanjiaMemberId,
			SecurityUtils.getCurrentUser().getId());
		long count = this.baseMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw new BusinessException(ResultEnum.KANJIA_ACTIVITY_LOG_MEMBER_ERROR);
		}
		// 校验当前砍价商品是否有效
		KanjiaActivityGoods kanjiaActivityGoods =
			kanJiaActivityGoodsService.getById(kanjiaActivityDTO.getKanjiaActivityGoodsId());
		// 如果当前活动不为空且还在活动时间内 才可以参与砍价活动
		if (kanjiaActivityGoods != null
			&& kanjiaActivityGoods.getPromotionStatus().equals(PromotionsStatusEnum.START.name())) {
			// 获取砍价参与者记录
			KanjiaActivity kanjiaActivity = kanJiaActivityService.getById(kanjiaActivityDTO.getKanjiaActivityId());
			if (kanjiaActivity != null) {
				KanjiaActivityLog kanJiaActivityLog = new KanjiaActivityLog();
				kanJiaActivityLog.setKanjiaActivityId(kanjiaActivity.getId());
				BeanUtil.copyProperties(kanjiaActivityDTO, kanJiaActivityLog);
				boolean result = this.save(kanJiaActivityLog);
				if (result) {
					return kanJiaActivityLog;
				}
			}
			throw new BusinessException(ResultEnum.KANJIA_ACTIVITY_NOT_FOUND_ERROR);
		}
		throw new BusinessException(ResultEnum.PROMOTION_STATUS_END);
	}

	@Override
	public KanjiaActivityLog queryKanjiaActivityLog(Long id, Long userId) {
		// 如果已发起砍价判断用户是否可以砍价
		return
			getOne(new LambdaQueryWrapper<KanjiaActivityLog>()
				.eq(KanjiaActivityLog::getKanjiaActivityId, id)
				.eq(KanjiaActivityLog::getKanjiaMemberId, userId));

	}
}
