package com.taotao.cloud.wechat.biz.wechat.core.menu.dao;

import cn.bootx.starter.wechat.core.menu.entity.WeChatMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信自定义菜单
 * @author xxm
 * @date 2022-08-08
 */
@Mapper
public interface WeChatMenuMapper extends BaseSuperMapper<WeChatMenu> {
}
