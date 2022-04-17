package com.taotao.cloud.promotion.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityDTO;
import com.taotao.cloud.promotion.api.dto.search.KanJiaActivityLogQuery;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivityLog;


/**
 * 砍价活动日志业务层
 */
public interface KanjiaActivityLogService extends IService<KanjiaActivityLog> {

    /**
     * 根据砍价参与记录id查询砍价记录
     *
     * @param kanJiaActivityLogQuery 砍价活动帮砍信息
     * @param pageVO                 分页信息
     * @return 砍价日志
     */
    IPage<KanjiaActivityLog> getForPage(KanJiaActivityLogQuery kanJiaActivityLogQuery, PageVO pageVO);

    /**
     * 砍一刀
     *
     * @param kanJiaActivityDTO 砍价记录
     * @return
     */
    KanjiaActivityLog addKanJiaActivityLog(KanjiaActivityDTO kanJiaActivityDTO);
}
