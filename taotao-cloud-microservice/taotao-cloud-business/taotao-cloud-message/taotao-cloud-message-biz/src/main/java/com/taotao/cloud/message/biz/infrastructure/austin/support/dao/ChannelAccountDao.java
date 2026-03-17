package com.taotao.cloud.message.biz.infrastructure.austin.support.dao;


import com.taotao.boot.data.jpa.base.repository.JpaSuperRepository;
import com.taotao.cloud.message.biz.infrastructure.austin.support.domain.ChannelAccount;

import java.util.List;

/**
 * 渠道账号信息 Dao
 *
 * @author shuigedeng
 */
public interface ChannelAccountDao extends JpaSuperRepository<ChannelAccount, Long> {


    /**
     * 查询 列表
     *
     * @param deleted     0：未删除 1：删除
     * @param channelType 渠道值
     * @param creator     创建者
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndCreatorEqualsAndSendChannelEquals(Integer deleted, String creator, Integer channelType);

    /**
     * 查询 列表
     *
     * @param deleted     0：未删除 1：删除
     * @param channelType 渠道值
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndSendChannelEquals(Integer deleted, Integer channelType);

    /**
     * 根据创建者检索相关的记录
     *
     * @param creator
     * @return
     */
    List<ChannelAccount> findAllByCreatorEquals(String creator);

    /**
     * 统计未删除的条数
     *
     * @param deleted
     * @return
     */
    Long countByIsDeletedEquals(Integer deleted);

}
