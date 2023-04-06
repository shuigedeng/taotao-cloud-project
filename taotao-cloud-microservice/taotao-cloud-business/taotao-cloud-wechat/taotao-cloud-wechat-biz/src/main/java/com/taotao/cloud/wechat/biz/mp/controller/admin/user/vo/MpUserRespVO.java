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

package com.taotao.cloud.wechat.biz.mp.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Data;

@ApiModel("管理后台 - 公众号粉丝 Response VO")
@Data
public class MpUserRespVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "公众号粉丝标识", required = true, example = "o6_bmjrPTlm6_2sgVt7hMZOPfL2M")
    private String openid;

    @ApiModelProperty(value = "关注状态", required = true, example = "1", notes = "参见 CommonStatusEnum 枚举")
    private Integer subscribeStatus;

    @ApiModelProperty(value = "关注时间", required = true)
    private LocalDateTime subscribeTime;

    @ApiModelProperty(value = "取消关注时间")
    private LocalDateTime unsubscribeTime;

    @ApiModelProperty(value = "昵称", example = "芋道")
    private String nickname;

    @ApiModelProperty(value = "头像地址", example = "https://www.iocoder.cn/1.png")
    private String headImageUrl;

    @ApiModelProperty(value = "语言", example = "zh_CN")
    private String language;

    @ApiModelProperty(value = "国家", example = "中国")
    private String country;

    @ApiModelProperty(value = "省份", example = "广东省")
    private String province;

    @ApiModelProperty(value = "城市", example = "广州市")
    private String city;

    @ApiModelProperty(value = "备注", example = "你是一个芋头嘛")
    private String remark;

    @ApiModelProperty(value = "标签编号数组", example = "1,2,3")
    private List<Long> tagIds;

    @ApiModelProperty(value = "公众号账号的编号", required = true, example = "1")
    private Long accountId;

    @ApiModelProperty(value = "公众号账号的 appId", required = true, example = "wx1234567890")
    private String appId;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;
}
