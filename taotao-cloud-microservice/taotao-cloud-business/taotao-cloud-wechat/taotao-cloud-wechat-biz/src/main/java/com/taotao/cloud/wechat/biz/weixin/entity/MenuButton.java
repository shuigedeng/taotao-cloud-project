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

package com.taotao.cloud.wechat.biz.weixin.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.*;

/**
 * 自定义菜单模型
 *
 * @author www.joolun.com
 */
@Data
public class MenuButton implements Serializable {

    private String type;

    private String name;

    private String key;

    private String url;

    private String media_id;

    private String appid;

    private String pagepath;

    private List<MenuButton> sub_button = new ArrayList();
    /** content内容 */
    private JSONObject content;

    private String repContent;
    /** 消息类型 */
    private String repType;
    /** 消息名 */
    private String repName;
    /** 视频和音乐的描述 */
    private String repDesc;
    /** 视频和音乐的描述 */
    private String repUrl;
    /** 高质量链接 */
    private String repHqUrl;
    /** 缩略图的媒体id */
    private String repThumbMediaId;
    /** 缩略图url */
    private String repThumbUrl;
}
