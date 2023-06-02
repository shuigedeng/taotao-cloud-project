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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.Query;
import com.github.niefy.modules.wx.dao.MsgTemplateMapper;
import com.github.niefy.modules.wx.entity.MsgTemplate;
import com.github.niefy.modules.wx.service.MsgTemplateService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service("msgTemplateService")
public class MsgTemplateServiceImpl extends ServiceImpl<MsgTemplateMapper, MsgTemplate> implements MsgTemplateService {
    @Autowired
    private WxMpService wxService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        String name = (String) params.get("name");
        String appid = (String) params.get("appid");
        IPage<MsgTemplate> page = this.page(
                new Query<MsgTemplate>().getPage(params),
                new QueryWrapper<MsgTemplate>()
                        .eq(StringUtils.hasText(appid), "appid", appid)
                        .like(StringUtils.hasText(title), "title", title)
                        .like(StringUtils.hasText(name), "name", name));

        return new PageUtils(page);
    }

    @Override
    public MsgTemplate selectByName(String name) {
        Assert.hasText(name, "模板名称不得为空");
        return this.getOne(
                new QueryWrapper<MsgTemplate>().eq("name", name).eq("status", 1).last("LIMIT 1"));
    }

    @Override
    public void syncWxTemplate(String appid) throws WxErrorException {
        List<WxMpTemplate> wxMpTemplateList = wxService.getTemplateMsgService().getAllPrivateTemplate();
        List<MsgTemplate> templates = wxMpTemplateList.stream()
                .map(item -> new MsgTemplate(item, appid))
                .toList();
        this.saveBatch(templates);
    }
}
