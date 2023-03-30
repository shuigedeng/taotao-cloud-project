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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.service;

import java.util.List;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;

public interface WxUserTagsService {
    /**
     * 获取公众号用户标签
     *
     * @return
     * @throws WxErrorException
     * @param appid
     */
    List<WxUserTag> getWxTags(String appid) throws WxErrorException;

    /**
     * 创建标签
     *
     * @param appid
     * @param name 标签名称
     */
    void creatTag(String appid, String name) throws WxErrorException;

    /**
     * 修改标签
     *
     * @param appid
     * @param tagid 标签ID
     * @param name 标签名称
     */
    void updateTag(String appid, Long tagid, String name) throws WxErrorException;

    /**
     * 删除标签
     *
     * @param appid
     * @param tagid 标签ID
     * @throws WxErrorException
     */
    void deleteTag(String appid, Long tagid) throws WxErrorException;

    /**
     * 批量给用户打标签
     *
     * @param appid
     * @param tagid 标签ID
     * @param openidList 用户openid列表
     * @throws WxErrorException
     */
    void batchTagging(String appid, Long tagid, String[] openidList) throws WxErrorException;

    /**
     * 批量取消用户标签
     *
     * @param appid
     * @param tagid 标签ID
     * @param openidList 用户openid列表
     * @throws WxErrorException
     */
    void batchUnTagging(String appid, Long tagid, String[] openidList) throws WxErrorException;

    /**
     * 为用户绑定标签
     *
     * @param tagid
     * @param openid
     * @throws WxErrorException
     */
    void tagging(Long tagid, String openid) throws WxErrorException;

    /**
     * 取消用户所绑定的某一个标签
     *
     * @param tagid
     * @param openid
     * @throws WxErrorException
     */
    void untagging(Long tagid, String openid) throws WxErrorException;
}
