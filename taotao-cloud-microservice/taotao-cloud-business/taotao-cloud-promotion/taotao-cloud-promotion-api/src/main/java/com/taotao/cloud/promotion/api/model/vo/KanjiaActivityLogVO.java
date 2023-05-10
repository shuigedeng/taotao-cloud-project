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

package com.taotao.cloud.promotion.api.model.vo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 砍价活动商品实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:50
 */
public class KanjiaActivityLogVO  {

    public static final String TABLE_NAME = "tt_kanjia_activity_log";
    /** 砍价活动参与记录id */
    private Long kanjiaActivityId;
    /** 砍价会员id */
    private Long kanjiaMemberId;
    /** 砍价会员名称 */
    private String kanjiaMemberName;
    /** 砍价会员头像 */
    private String kanjiaMemberFace;
    /** 砍价金额 */
    private BigDecimal kanjiaPrice;
    /** 剩余购买金额 */
    private BigDecimal surplusPrice;

}
