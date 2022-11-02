/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.payment.biz.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@ApiModel(value = "商户渠道表")
public class PayChannel implements Serializable {

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private Integer id;

	/**
	 * 应用id
	 */
	@ApiModelProperty(value = "应用id")
	private String appId;

	/**
	 * 提供商模式商户id
	 */
	@ApiModelProperty(value = "提供商模式商户id")
	private String mchId;

	/**
	 * 渠道商id
	 */
	@ApiModelProperty(value = "渠道商id")
	private String channelId;

	/**
	 * 渠道商名称
	 */
	@ApiModelProperty(value = "渠道商名称")
	private String channelName;

	/**
	 * 渠道商商户id
	 */
	@ApiModelProperty(value = "渠道商商户id")
	private String channelMchId;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 0: 禁用 1：启用
	 */
	@ApiModelProperty(value = "0: 禁用 1：启用")
	private Integer delFlag;

	/**
	 * 租户id
	 */
	@ApiModelProperty(value = "租户id")
	private Integer tenantId;

	/**
	 * json格式的配置
	 */
	@ApiModelProperty(value = "json格式的配置")
	private String config;

	private static final long serialVersionUID = 1L;
}
