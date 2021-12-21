package com.taotao.cloud.member.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.cloud.member.biz.entity.FootPrint;
import java.util.List;

/**
 * 会员浏览历史业务层
 *
 *
 * @since 2020/11/18 10:46 上午
 */
public interface FootprintService extends IService<FootPrint> {

    /**
     * 保存浏览历史
     *
     * @param footPrint 用户足迹
     * @return 浏览历史
     */
    FootPrint saveFootprint(FootPrint footPrint);

    /**
     * 清空当前会员的足迹
     *
     * @return 处理结果
     */
    boolean clean();

    /**
     * 根据ID进行清除会员的历史足迹
     *
     * @param ids 商品ID列表
     * @return 处理结果
     */
    boolean deleteByIds(List<String> ids);

    /**
     * 获取会员浏览历史分页
     *
     * @param pageVO 分页
     * @return 会员浏览历史列表
     */
    List<EsGoodsIndex> footPrintPage(PageVO pageVO);

    /**
     * 获取当前会员的浏览记录数量
     *
     * @return 当前会员的浏览记录数量
     */
    long getFootprintNum();
}
