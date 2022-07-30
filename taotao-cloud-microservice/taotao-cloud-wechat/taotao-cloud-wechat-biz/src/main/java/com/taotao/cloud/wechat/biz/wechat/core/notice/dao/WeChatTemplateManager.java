package com.taotao.cloud.wechat.biz.wechat.core.notice.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wechat.core.notice.entity.WeChatTemplate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
*
* @author xxm
* @date 2022/7/17
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeChatTemplateManager extends BaseManager<WeChatTemplateMapper, WeChatTemplate> {

    public Page<WeChatTemplate> page(PageParam pageParam) {
        Page<WeChatTemplate> mpPage = MpUtil.getMpPage(pageParam, WeChatTemplate.class);
        return this.lambdaQuery()
                .select(WeChatTemplate.class, MpUtil::excludeBigField)
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }

    /**
     * 根据id停用
     */
    public void disableByTemplateIds(List<String> templateIds) {
        this.lambdaUpdate()
                .set(WeChatTemplate::isEnable,false)
                .in(WeChatTemplate::getTemplateId,templateIds)
                .update();
    }
    /**
     * 根据id启用
     */
    public void enableByTemplateIds(List<String> templateIds) {
        this.lambdaUpdate()
                .set(WeChatTemplate::isEnable,true)
                .in(WeChatTemplate::getTemplateId,templateIds)
                .update();
    }
}
