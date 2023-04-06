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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.service.impl;

import com.github.niefy.modules.wx.service.WxUserService;
import com.github.niefy.modules.wx.service.WxUserTagsService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {"wxUserTagsServiceCache"})
@Slf4j
public class WxUserTagsServiceImpl implements WxUserTagsService {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxUserService wxUserService;

    public static final String CACHE_KEY = "'WX_USER_TAGS'";

    @Override
    @Cacheable(key = CACHE_KEY + "+ #appid")
    public List<WxUserTag> getWxTags(String appid) throws WxErrorException {
        log.info("拉取公众号用户标签");
        wxMpService.switchoverTo(appid);
        return wxMpService.getUserTagService().tagGet();
    }

    @Override
    @CacheEvict(key = CACHE_KEY + "+ #appid")
    public void creatTag(String appid, String name) throws WxErrorException {
        wxMpService.switchoverTo(appid);
        wxMpService.getUserTagService().tagCreate(name);
    }

    @Override
    @CacheEvict(key = CACHE_KEY + "+ #appid")
    public void updateTag(String appid, Long tagid, String name) throws WxErrorException {
        wxMpService.switchoverTo(appid);
        wxMpService.getUserTagService().tagUpdate(tagid, name);
    }

    @Override
    @CacheEvict(key = CACHE_KEY + "+ #appid")
    public void deleteTag(String appid, Long tagid) throws WxErrorException {
        wxMpService.switchoverTo(appid);
        wxMpService.getUserTagService().tagDelete(tagid);
    }

    @Override
    public void batchTagging(String appid, Long tagid, String[] openidList) throws WxErrorException {
        wxMpService.switchoverTo(appid);
        wxMpService.getUserTagService().batchTagging(tagid, openidList);
        wxUserService.refreshUserInfoAsync(openidList, appid); // 标签更新后更新对应用户信息
    }

    @Override
    public void batchUnTagging(String appid, Long tagid, String[] openidList) throws WxErrorException {
        wxMpService.switchoverTo(appid);
        wxMpService.getUserTagService().batchUntagging(tagid, openidList);
        wxUserService.refreshUserInfoAsync(openidList, appid); // 标签更新后更新对应用户信息
    }

    @Override
    public void tagging(Long tagid, String openid) throws WxErrorException {
        wxMpService.getUserTagService().batchTagging(tagid, new String[] {openid});
        String appid = WxMpConfigStorageHolder.get();
        wxUserService.refreshUserInfo(openid, appid);
    }

    @Override
    public void untagging(Long tagid, String openid) throws WxErrorException {
        wxMpService.getUserTagService().batchUntagging(tagid, new String[] {openid});
        String appid = WxMpConfigStorageHolder.get();
        wxUserService.refreshUserInfo(openid, appid);
    }
}
