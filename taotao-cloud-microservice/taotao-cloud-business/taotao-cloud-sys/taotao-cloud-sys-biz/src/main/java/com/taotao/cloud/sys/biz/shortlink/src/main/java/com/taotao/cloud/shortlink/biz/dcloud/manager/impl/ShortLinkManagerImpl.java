package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.ShortLinkManager;
import net.xdclass.mapper.ShortLinkMapper;
import net.xdclass.model.ShortLinkDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShortLinkManagerImpl implements ShortLinkManager {

    @Autowired
    private ShortLinkMapper shortLinkMapper;


    /**
     * 新增
     * @param shortLinkDO
     * @return
     */
    @Override
    public int addShortLink(ShortLinkDO shortLinkDO) {
        return shortLinkMapper.insert(shortLinkDO);
    }


    /**
     * 根据短链码找短链
     * @param shortLinkCode
     * @return
     */
    @Override
    public ShortLinkDO findByShortLinkCode(String shortLinkCode) {
        ShortLinkDO shortLinkDO = shortLinkMapper.selectOne(new QueryWrapper<ShortLinkDO>()
                .eq("code", shortLinkCode).eq("del",0));
        return shortLinkDO;
    }


    /**
     * 冗余双写C端-删除操作
     * @param shortLinkDO
     * @return
     */
    @Override
    public int del(ShortLinkDO shortLinkDO) {
        //逻辑删除
        int rows = shortLinkMapper.update(null,
                new UpdateWrapper<ShortLinkDO>()
                        .eq("code", shortLinkDO.getCode())
                        .eq("account_no", shortLinkDO.getAccountNo())
                        .set("del",1));
        return rows;
    }


    /**
     * 冗余双写C端-更新操作
     * @param shortLinkDO
     * @return
     */
    @Override
    public int update(ShortLinkDO shortLinkDO) {
        int rows = shortLinkMapper.update(null, new UpdateWrapper<ShortLinkDO>()
                .eq("code", shortLinkDO.getCode())
                .eq("del", 0)
                .eq("account_no",shortLinkDO.getAccountNo())
                .set("title", shortLinkDO.getTitle())
                .set("domain", shortLinkDO.getDomain()));

        return rows;
    }

}
