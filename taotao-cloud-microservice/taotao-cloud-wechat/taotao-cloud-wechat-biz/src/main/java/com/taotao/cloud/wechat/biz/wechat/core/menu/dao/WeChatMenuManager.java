package com.taotao.cloud.wechat.biz.wechat.core.menu.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wechat.core.menu.entity.WeChatMenu;
import cn.bootx.starter.wechat.param.menu.WeChatMenuParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 微信自定义菜单
 * @author xxm
 * @date 2022-08-08
 */
@Repository
@RequiredArgsConstructor
public class WeChatMenuManager extends BaseManager<WeChatMenuMapper, WeChatMenu> {

    /**
    * 分页
    */
    public Page<WeChatMenu> page(PageParam pageParam, WeChatMenuParam param) {
        Page<WeChatMenu> mpPage = MpUtil.getMpPage(pageParam, WeChatMenu.class);
        return lambdaQuery()
                .select(this.getEntityClass(),MpUtil::excludeBigField)
                .eq(StrUtil.isNotBlank(param.getName()),WeChatMenu::getName,param.getName())
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }

    /**
     * 清除其他发布状态
     */
    public void clearPublish(){
        lambdaUpdate()
                .eq(WeChatMenu::isPublish,true)
                .set(WeChatMenu::isPublish,false)
                .update();
    }
}
