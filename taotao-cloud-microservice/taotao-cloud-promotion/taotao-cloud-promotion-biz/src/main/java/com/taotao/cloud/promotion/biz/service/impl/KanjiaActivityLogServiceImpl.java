package com.taotao.cloud.promotion.biz.service.impl;


import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityDTO;
import com.taotao.cloud.promotion.api.query.KanJiaActivityLogQuery;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivityGoods;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivityLog;
import com.taotao.cloud.promotion.biz.mapper.KanJiaActivityLogMapper;
import com.taotao.cloud.promotion.biz.service.KanjiaActivityGoodsService;
import com.taotao.cloud.promotion.biz.service.KanjiaActivityLogService;
import com.taotao.cloud.promotion.biz.service.KanjiaActivityService;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 砍价活动日志业务层实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KanjiaActivityLogServiceImpl extends ServiceImpl<KanJiaActivityLogMapper, KanjiaActivityLog> implements
	KanjiaActivityLogService {

    @Autowired
    private KanjiaActivityGoodsService kanJiaActivityGoodsService;

    @Autowired
    private KanjiaActivityService kanJiaActivityService;

    @Override
    public IPage<KanjiaActivityLog> getForPage(KanJiaActivityLogQuery kanJiaActivityLogQuery, PageVO pageVO) {
        QueryWrapper<KanjiaActivityLog> queryWrapper = kanJiaActivityLogQuery.wrapper();
        return this.page(PageUtil.initPage(pageVO), queryWrapper);
    }


    @Override
    public KanjiaActivityLog addKanJiaActivityLog(KanjiaActivityDTO kanjiaActivityDTO) {
        //校验当前会员是否已经参与过此次砍价
        LambdaQueryWrapper<KanjiaActivityLog> queryWrapper = new LambdaQueryWrapper<KanjiaActivityLog>();
        queryWrapper.eq(kanjiaActivityDTO.getKanjiaActivityId() != null, KanjiaActivityLog::getKanjiaActivityId, kanjiaActivityDTO.getKanjiaActivityId());
        queryWrapper.eq( KanjiaActivityLog::getKanjiaMemberId, UserContext.getCurrentUser().getId());
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultEnum.KANJIA_ACTIVITY_LOG_MEMBER_ERROR);
        }
        //校验当前砍价商品是否有效
        KanjiaActivityGoods kanjiaActivityGoods = kanJiaActivityGoodsService.getById(kanjiaActivityDTO.getKanjiaActivityGoodsId());
        //如果当前活动不为空且还在活动时间内 才可以参与砍价活动
        if (kanjiaActivityGoods != null && kanjiaActivityGoods.getPromotionStatus().equals(
	        PromotionsStatusEnum.START.name())) {
            //获取砍价参与者记录
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
}
