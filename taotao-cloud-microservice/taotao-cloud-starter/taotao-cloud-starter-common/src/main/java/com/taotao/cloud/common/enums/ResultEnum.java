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
package com.taotao.cloud.common.enums;

import com.taotao.cloud.common.utils.date.DateUtil;

import java.time.LocalDateTime;

/**
 * 返回结果枚举 code规则 500(开始累加)  + 自增三位数(000 开始累加)
 * <p>
 * 500 -> 公共错误编码
 * <p>
 * 501 -> 系统管理错误编码
 * 502 -> 订单管理错误编码
 * 503 -> 商品管理错误编码
 * 504 -> 支付管理错误编码
 * <p>
 * ...
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:25:00
 */
public enum ResultEnum implements BaseEnum {
	//********************************************* 公共枚举参数 *******************************************************
	SUCCESS(200, "请求成功"),
	ERROR(500, "系统错误，请稍后重试"),
	LOGIN_SUCCESS(200, "登录成功"),
	LOGOUT_SUCCESS(200, "退出成功"),
	BAD_REQUEST(400, "请求错误"),
	UNAUTHORIZED(401, "用户未登录认证失败"),
	FORBIDDEN(403, "用户权限不足"),
	REQUEST_NOT_FOUND(404, "请求不存在"),
	INNER_ERROR(500, "内部系统调用错误"),
	SQL_INJECTION_REQUEST(405, "疑似SQL注入请求"),

	//********************************************* 其他异常 *******************************************************
	CUSTOM_WORDS_EXIST_ERROR(599001, "当前自定义分词已存在！"),
	CUSTOM_WORDS_NOT_EXIST_ERROR(599002, "当前自定义分词不存在！"),
	CUSTOM_WORDS_SECRET_KEY_ERROR(599003, "秘钥验证失败！"),
	CONNECT_NOT_EXIST(599004, "登录方式不存在！"),
	ELASTICSEARCH_INDEX_INIT_ERROR(599005, "索引初始化失败！"),
	PURCHASE_ORDER_DEADLINE_ERROR(599006, "供求单，已超过报名截止时间"),
	INDEX_BUILDING(599007, "索引正在生成"),

	//********************************************* 系统异常 *******************************************************
	USER_EDIT_SUCCESS(50000, "用户修改成功"),
	USER_NOT_LOGIN(500002, "用户未登录"),
	USER_AUTH_EXPIRED(500003, "用户已退出，请重新登录"),
	USER_AUTHORITY_ERROR(500004, "权限不足"),
	USER_CONNECT_LOGIN_ERROR(500005, "未找到登录信息"),
	USER_EXIST(500006, "该用户名或手机号已被注册"),
	USER_PHONE_NOT_EXIST(500007, "手机号不存在"),
	USER_PASSWORD_ERROR(500008, "密码不正确"),
	USER_NOT_PHONE(500009, "非当前用户的手机号"),
	USER_CONNECT_ERROR(5000010, "联合第三方登录，授权信息错误"),
	USER_RECEIPT_REPEAT_ERROR(500011, "会员发票信息重复"),
	USER_RECEIPT_NOT_EXIST(500012, "会员发票信息不存在"),
	USER_EDIT_ERROR(500013, "用户修改失败"),
	USER_OLD_PASSWORD_ERROR(500014, "旧密码不正确"),
	USER_COLLECTION_EXIST(500015, "无法重复收藏"),
	USER_GRADE_IS_DEFAULT(500016, "会员等级为默认会员等级"),
	USER_NOT_BINDING(500017, "未绑定用户"),
	USER_AUTO_REGISTER_ERROR(500018, "自动注册失败,请稍后重试"),
	USER_OVERDUE_CONNECT_ERROR(500019, "授权信息已过期，请重新授权/登录"),
	USER_CONNECT_BANDING_ERROR(5000020, "当前联合登陆方式，已绑定其他账号，需进行解绑操作"),
	USER_CONNECT_NOT_EXIST_ERROR(500021, "暂无联合登陆信息，无法实现一键注册功能，请点击第三方登录进行授权"),
	USER_POINTS_ERROR(500022, "用户积分不足"),
	USERNAME_OR_PASSWORD_ERROR(500023, "用户名或密码错误"),
	USER_NOT_EXIST(500024, "用户不存在"),
	USER_NAME_EXIST(500025, "用户名称不存在"),
	USER_PHONE_EXIST(500026, "用户手机不存在"),
	USER_DISABLE(500027, "账号已被禁用"),
	USER_DELETED(500028, "账号已删除"),
	VERIFY_CODE_ERROR(500029, "验证码错误"),
	CODE_ERROR(500030, "验证码异常"),
	CODE_GET_ERROR(500031, "获取验证码的值失败"),
	CODE_NOT_FOUND(500032, "验证码不存在"),
	CODE_IS_EXPIRED(500033, "验证码已过期"),
	CODE_NOT_MATCH(500034, "验证码不匹配"),
	CODE_SEND_ERROR(500035, "验证码发送错误"),
	ILLEGAL_ARGUMENT_ERROR(500036, "参数认证错误"),
	VERIFY_ARGUMENT_ERROR(500037, "参数校验错误"),
	MESSAGE_SEND_ERROR(500038, "消息发送错误"),
	METHOD_NOT_SUPPORTED_ERROR(500039, "不支持当前请求方法"),
	MEDIA_TYPE_NOT_SUPPORTED_ERROR(500040, "不支持当前媒体类型"),
	SQL_ERROR(500051, "sql错误"),
	DICT_CODE_REPEAT_ERROR(500052, "字典code已存在"),
	USER_PHONE_EXISTS_ERROR(500053, "用户手机已存在"),
	USER_PHONE_INCONSISTENT_ERROR(500054, "用户手机不一致"),
	DICT_NOT_EXIST(500055, "字典数据不存在"),
	MEMBER_NICKNAME_EXIST(500056, "昵称已存在"),
	MEMBER_PHONE_EXIST(500057, "手机已存在"),
	USER_UNAUTHORIZED_ERROR(500058, "用户认证失败"),
	ROLE_NOT_EXIST(500059, "角色不存在"),
	RESOURCE_NOT_EXIST(500060, "资源不存在"),
	PRODUCT_NOT_EXIST(500061, "商品不存在"),
	RESOURCE_NAME_EXISTS_ERROR(500062, "资源名称已存在"),
	FILE_NOT_EXIST(500063, "文件不存在"),
	MEMBER_LOGIN_NOT_EXIST(500064, "会员登录日志不存在"),
	EXPRESS_COMPANY_NOT_EXIST(500065, "物流公司不存在"),
	EMAIL_NOT_EXIST(500066, "邮件信息不存在"),
	PAY_FLOW_NOT_EXIST(500067, "支付信息不存在"),
	MEMBER_NOT_EXIST(500068, "会员用户信息不存在"),
	METHOD_ARGUMENTS_TYPE_MISMATCH(500069, "参数类型不匹配,传入参数格式不正确或参数解析异常"),
	MISSING_SERVLET_REQUEST_PARAMETER(500070, "缺少参数"),
	CODE_VALUE_NOT_NULL(500071, "验证码的值不能为空"),

	HTTP_REQUEST_METHOD_NOT_SUPPORTED(500001, "请求method不匹配"),
	HTTP_MESSAGE_NOT_READABLE(500001, "类型参数数据类型转换异常"),
	// 微信
	WECHAT_CONNECT_NOT_EXIST(500001, "微信联合登录未配置"),
	VERIFICATION_EXIST(500001, "验证码服务异常"),
	LIMIT_ERROR(500001, "访问过于频繁，请稍后再试"),
	ILLEGAL_REQUEST_ERROR(500001, "非法请求，请重新刷新页面操作"),
	IMAGE_FILE_EXT_ERROR(500001, "不支持图片格式"),
	FILE_TYPE_NOT_SUPPORT(500001, "不支持上传的文件类型！"),
	PLATFORM_NOT_SUPPORTED_IM(500001, "平台未开启IM"),
	STORE_NOT_SUPPORTED_IM(500001, "店铺未开启IM"),
	WECHAT_CONNECT_NOT_SETTING(500001, "微信联合登陆信息未配置"),
	WECHAT_PAYMENT_NOT_SETTING(500001, "微信支付信息未配置"),
	WECHAT_QRCODE_ERROR(500001, "微信二维码生成异常"),
	WECHAT_MP_MESSAGE_ERROR(500001, "微信小程序小消息订阅异常"),
	WECHAT_JSAPI_SIGN_ERROR(500001, "微信JsApi签名异常"),
	WECHAT_CERT_ERROR(500001, "证书获取失败"),
	WECHAT_MP_MESSAGE_TMPL_ERROR(500001, "未能获取到微信模版消息id"),
	WECHAT_ERROR(500001, "微信接口异常"),
	APP_VERSION_EXIST(500001, "APP版本已存在"),

	PERMISSION_DEPARTMENT_ROLE_ERROR(500001, "角色已绑定部门，请逐个删除"),
	PERMISSION_USER_ROLE_ERROR(500001, "角色已绑定管理员，请逐个删除"),
	PERMISSION_MENU_ROLE_ERROR(500001, "菜单已绑定角色，请先删除或编辑角色"),
	PERMISSION_DEPARTMENT_DELETE_ERROR(500001, "部门已经绑定管理员，请先删除或编辑管理员"),
	PERMISSION_BEYOND_TEN(500001, "最多可以设置10个角色"),
	//设置
	SETTING_NOT_TO_SET(500001, "该参数不需要设置"),
	ALIPAY_NOT_SETTING(500001, "支付宝支付未配置"),
	ALIPAY_EXCEPTION(500001, "支付宝支付错误，请稍后重试"),
	ALIPAY_PARAMS_EXCEPTION(500001, "支付宝参数异常"),
	LOGISTICS_NOT_SETTING(500001, "您还未配置快递查询"),
	ORDER_SETTING_ERROR(500001, "系统订单配置异常"),
	Att_SMS_SETTING_ERROR(500001, "您还未配置阿里云短信"),
	SMS_SIGN_EXIST_ERROR(500001, "短信签名已存在"),
	//验证码
	VERIFICATION_SEND_SUCCESS(500001, "短信验证码,发送成功"),
	VERIFICATION_ERROR(500001, "验证失败"),
	VERIFICATION_CODE_INVALID(500001, "验证码已失效，请重新校验"),
	VERIFICATION_SMS_CHECKED_ERROR(500001, "短信验证码错误，请重新校验"),
	//OSS
	OSS_NOT_EXIST(500001, "OSS未配置"),
	OSS_EXCEPTION_ERROR(500001, "文件上传失败，请稍后重试"),
	OSS_DELETE_ERROR(500001, "图片删除失败"),

	//********************************************* 商品模块异常 *******************************************************
	// 分类
	CATEGORY_NOT_EXIST(501001, "商品分类不存在"),
	CATEGORY_NAME_IS_EXIST(501001, "该分类名称已存在"),
	CATEGORY_PARENT_NOT_EXIST(501001, "该分类名称已存在"),
	CATEGORY_BEYOND_THREE(501001, "最多为三级分类,添加失败"),
	CATEGORY_HAS_CHILDREN(501001, "此类别下存在子类别不能删除"),
	CATEGORY_HAS_GOODS(501001, "此类别下存在商品不能删除"),
	CATEGORY_SAVE_ERROR(501001, "此类别下存在商品不能删除"),
	CATEGORY_PARAMETER_NOT_EXIST(501001, "分类绑定参数组不存在"),
	CATEGORY_PARAMETER_SAVE_ERROR(501001, "分类绑定参数组添加失败"),
	CATEGORY_PARAMETER_UPDATE_ERROR(501001, "分类绑定参数组添加失败"),
	CATEGORY_DELETE_FLAG_ERROR(501001, "子类状态不能与父类不一致！"),
	CATEGORY_COMMISSION_RATE_ERROR(501001, "分类的佣金不正确！"),
	// 商品
	GOODS_ERROR(501001, "商品异常，请稍后重试"),
	GOODS_NOT_EXIST(501001, "商品已下架"),
	GOODS_NAME_ERROR(501001, "商品名称不正确，名称应为2-50字符"),
	GOODS_UNDER_ERROR(501001, "商品下架失败"),
	GOODS_UPPER_ERROR(501001, "商品上架失败"),
	GOODS_AUTH_ERROR(501001, "商品审核失败"),
	POINT_GOODS_ERROR(501001, "积分商品业务异常，请稍后重试"),
	POINT_GOODS_NOT_EXIST(501001, "积分商品不存在"),
	POINT_GOODS_CATEGORY_EXIST(501001, "当前积分商品分类已存在"),
	GOODS_SKU_SN_ERROR(501001, "商品SKU货号不能为空"),
	GOODS_SKU_PRICE_ERROR(501001, "商品SKU价格不能小于等于0"),
	GOODS_SKU_COST_ERROR(501001, "商品SKU成本价不能小于等于0"),
	GOODS_SKU_WEIGHT_ERROR(501001, "商品重量不能为负数"),
	GOODS_SKU_QUANTITY_ERROR(501001, "商品库存数量不能为负数"),
	GOODS_SKU_QUANTITY_NOT_ENOUGH(501001, "商品库存不足"),
	MUST_HAVE_GOODS_SKU(501001, "规格必须要有一个！"),
	GOODS_PARAMS_ERROR(501001, "商品参数错误，刷新后重试"),
	PHYSICAL_GOODS_NEED_TEMP(501001, "实物商品需选择配送模板"),
	VIRTUAL_GOODS_NOT_NEED_TEMP(501001, "实物商品需选择配送模板"),
	GOODS_NOT_EXIST_STORE(501001, "当前用户无权操作此商品"),
	GOODS_TYPE_ERROR(501001, "需选择商品类型"),
	// 参数
	PARAMETER_SAVE_ERROR(501001, "参数添加失败"),
	PARAMETER_UPDATE_ERROR(501001, "参数编辑失败"),
	//规格
	SPEC_SAVE_ERROR(501001, "规格修改失败"),
	SPEC_UPDATE_ERROR(501001, "规格修改失败"),
	SPEC_DELETE_ERROR(501001, "分类已经绑定此规格，请先解除关联"),
	//品牌
	BRAND_SAVE_ERROR(501001, "品牌添加失败"),
	BRAND_UPDATE_ERROR(501001, "品牌修改失败"),
	BRAND_DISABLE_ERROR(501001, "品牌禁用失败"),
	BRAND_DELETE_ERROR(501001, "品牌删除失败"),
	BRAND_NAME_EXIST_ERROR(501001, "品牌名称重复"),
	BRAND_USE_DISABLE_ERROR(501001, "分类已经绑定品牌，请先解除关联"),
	BRAND_BIND_GOODS_ERROR(501001, "品牌已经绑定商品，请先解除关联"),
	BRAND_NOT_EXIST(501001, "品牌不存在"),
	//评价
	EVALUATION_DOUBLE_ERROR(501001, "无法重复提交评价"),

	//********************************************* 分销模块异常 *******************************************************
	DISTRIBUTION_CLOSE(502001, "分销功能关闭"),
	DISTRIBUTION_NOT_EXIST(502002, "分销员不存在"),
	DISTRIBUTION_IS_APPLY(502002, "分销员已申请，无需重复提交"),
	DISTRIBUTION_AUDIT_ERROR(502002, "审核分销员失败"),
	DISTRIBUTION_RETREAT_ERROR(502002, "分销员清退失败"),
	DISTRIBUTION_CASH_NOT_EXIST(502002, "分销员提现记录不存在"),
	DISTRIBUTION_GOODS_DOUBLE(502002, "不能重复添加分销商品"),

	//********************************************* 购物车模块异常 *******************************************************
	CART_ERROR(503001, "读取结算页的购物车异常"),
	CART_NUM_ERROR(503001, "购买数量必须大于0"),
	CART_PINTUAN_NOT_EXIST_ERROR(503001, "拼团活动已关闭，请稍后重试"),
	CART_PINTUAN_LIMIT_ERROR(503001, "购买数量超过拼团活动限制数量"),
	SHIPPING_NOT_APPLY(503001, "购物商品不支持当前收货地址配送"),

	//********************************************* 订单模块异常 *******************************************************
	ORDER_ERROR(504001, "创建订单异常，请稍后重试"),
	ORDER_NOT_EXIST(504001, "订单不存在"),
	ORDER_DELIVERED_ERROR(504001, "订单状态错误，无法进行确认收货"),
	ORDER_UPDATE_PRICE_ERROR(504001, "已支付的订单不能修改金额"),
	ORDER_LOGISTICS_ERROR(504001, "物流错误"),
	ORDER_DELIVER_ERROR(504001, "物流错误"),
	ORDER_NOT_USER(504001, "非当前会员的订单"),
	ORDER_TAKE_ERROR(504001, "当前订单无法核销"),
	MEMBER_ADDRESS_NOT_EXIST(504001, "订单无收货地址，请先配置收货地址"),
	ORDER_DELIVER_NUM_ERROR(504001, "没有待发货的订单"),
	ORDER_NOT_SUPPORT_DISTRIBUTION(504001, "购物车中包含不支持配送的商品，请重新选择收货地址，或者重新选择商品"),
	ORDER_CAN_NOT_CANCEL(504001, "当前订单状态不可取消"),
	ORDER_BATCH_DELIVER_ERROR(504001, "批量发货,文件读取失败"),
	ORDER_ITEM_NOT_EXIST(504001, "当前订单项不存在！"),
	POINT_NOT_ENOUGH(504001, "当前会员积分不足购买当前积分商品！"),

	//********************************************* 支付模块异常 *******************************************************
	PAY_UN_WANTED(505001, "当前订单不需要付款，返回订单列表等待系统订单出库即可"),
	PAY_SUCCESS(505001, "支付成功"),
	PAY_INCONSISTENT_ERROR(505001, "付款金额和应付金额不一致"),
	PAY_DOUBLE_ERROR(505001, "订单已支付，不能再次进行支付"),
	PAY_CASHIER_ERROR(505001, "收银台信息获取错误"),
	PAY_ERROR(505001, "支付业务异常，请稍后重试"),
	PAY_BAN(505001, "当前订单不需要付款，请返回订单列表重新操作"),
	PAY_PARTIAL_ERROR(505001, "该订单已部分支付，请前往订单中心进行支付"),
	PAY_NOT_SUPPORT(505001, "支付暂不支持"),
	PAY_CLIENT_TYPE_ERROR(505001, "错误的客户端"),
	PAY_POINT_ENOUGH(505001, "积分不足，不能兑换"),
	PAY_NOT_EXIST_ORDER(505001, "支付订单不存在"),
	CAN_NOT_RECHARGE_WALLET(505001, "不能使用余额进行充值"),

	//********************************************* 售后模块异常 *******************************************************
	AFTER_SALES_NOT_PAY_ERROR(506001, "当前订单未支付，不能申请售后"),
	AFTER_SALES_CANCEL_ERROR(506001, "当前售后单无法取消"),
	AFTER_SALES_BAN(506001, "订单状态不允许申请售后，请联系平台或商家"),
	AFTER_SALES_DOUBLE_ERROR(506001, "售后已审核，无法重复操作"),
	AFTER_SALES_LOGISTICS_ERROR(506001, "物流公司错误，请重新选择"),
	AFTER_STATUS_ERROR(506001, "售后状态错误，请刷新页面"),
	RETURN_MONEY_OFFLINE_BANK_ERROR(506001, "当账号类型为银行转账时，银行信息不能为空"),
	AFTER_SALES_PRICE_ERROR(506001, "申请退款金额错误"),
	AFTER_GOODS_NUMBER_ERROR(506001, "申请售后商品数量错误"),

	//********************************************* 投诉模块异常 *******************************************************
	COMPLAINT_ORDER_ITEM_EMPTY_ERROR(507001, "订单不存在"),
	COMPLAINT_SKU_EMPTY_ERROR(507001, "商品已下架，如需投诉请联系平台客服"),
	COMPLAINT_ERROR(507001, "投诉异常，请稍后重试"),
	COMPLAINT_NOT_EXIT(507001, "当前投诉记录不存在"),
	COMPLAINT_ARBITRATION_RESULT_ERROR(507001, "结束订单投诉时，仲裁结果不能为空"),
	COMPLAINT_APPEAL_CONTENT_ERROR(507001, "商家申诉时，申诉内容不能为空"),
	COMPLAINT_CANCEL_ERROR(507001, "申诉已完成，不需要进行取消申诉操作"),

	//********************************************* 余额模块异常 *******************************************************
	WALLET_NOT_EXIT_ERROR(508001, "钱包不存在，请联系管理员"),
	WALLET_INSUFFICIENT(508001, "余额不足以支付订单，请充值!"),
	WALLET_WITHDRAWAL_INSUFFICIENT(508001, "可提现金额不足！"),
	WALLET_WITHDRAWAL_FROZEN_AMOUNT_INSUFFICIENT(508001, "冻结金额不足，无法处理提现申请请求！"),
	WALLET_ERROR_INSUFFICIENT(508001, "零钱提现失败！"),
	WALLET_REMARK_ERROR(508001, "请填写审核备注！"),
	WALLET_EXIT_ERROR(508001, "钱包已存在，无法重复创建"),
	WALLET_APPLY_ERROR(508001, "提现申请异常！"),

	//********************************************* 活动模块异常 *******************************************************
	PROMOTION_GOODS_NOT_EXIT(509001, "当前促销商品不存在！"),
	PROMOTION_GOODS_QUANTITY_NOT_EXIT(509001, "当前促销商品库存不足！"),
	PROMOTION_SAME_ACTIVE_EXIST(509001, "活动时间内已存在同类活动，请选择关闭、删除当前时段的活动"),
	PROMOTION_START_TIME_ERROR(509001, "活动起始时间不能小于当前时间"),
	PROMOTION_END_TIME_ERROR(509001, "活动结束时间不能小于当前时间"),
	PROMOTION_TIME_ERROR(509001, "活动起始时间必须大于结束时间"),
	PROMOTION_TIME_NOT_EXIST(509001, "活动起始时间和活动结束时间不能为空"),
	PROMOTION_SAME_ERROR(509001, "当前时间段已存在相同活动！"),
	PROMOTION_GOODS_ERROR(509001, "请选择要参与活动的商品"),
	PROMOTION_STATUS_END(509001, "当前活动已停止"),
	PROMOTION_UPDATE_ERROR(509001, "当前活动已开始/结束，无法编辑！"),
	PROMOTION_ACTIVITY_GOODS_ERROR(509001, "当前活动已经开始无法添加商品"),
	PROMOTION_ACTIVITY_ERROR(509001, "当前促销活动不存在"),
	PROMOTION_LOG_EXIST(509001, "活动已参加，已发重复参加"),

	//********************************************* 优惠券模块异常 *******************************************************
	COUPON_LIMIT_ERROR(510001, "超出领取限制"),
	COUPON_EDIT_STATUS_SUCCESS(510001, "修改状态成功！"),
	COUPON_CANCELLATION_SUCCESS(510001, "会员优惠券作废成功"),
	COUPON_EXPIRED(510001, "优惠券已使用/已过期，不能使用"),
	COUPON_EDIT_STATUS_ERROR(510001, "优惠券修改状态失败！"),
	COUPON_RECEIVE_ERROR(510001, "当前优惠券已经被领取完了，下次要早点来哦"),
	COUPON_NUM_INSUFFICIENT_ERROR(510001, "优惠券剩余领取数量不足"),
	COUPON_NOT_EXIST(510001, "当前优惠券不存在"),
	COUPON_DO_NOT_RECEIVER(510001, "当前优惠券不允许主动领取"),
	COUPON_ACTIVITY_NOT_EXIST(510001, "当前优惠券活动不存在"),
	COUPON_SAVE_ERROR(510001, "保存优惠券失败"),
	COUPON_ACTIVITY_SAVE_ERROR(510001, "保存优惠券活动失败"),
	COUPON_DELETE_ERROR(510001, "删除优惠券失败"),
	COUPON_LIMIT_NUM_LESS_THAN_0(510001, "领取限制数量不能为负数"),
	COUPON_LIMIT_GREATER_THAN_PUBLISH(510001, "领取限制数量超出发行数量"),
	COUPON_DISCOUNT_ERROR(510001, "优惠券折扣必须小于10且大于0"),
	COUPON_SCOPE_TYPE_GOODS_ERROR(510001, "当前关联范围类型为指定商品时，商品列表不能为空"),
	COUPON_SCOPE_TYPE_CATEGORY_ERROR(510001, "当前关联范围类型为部分商品分类时，范围关联的id不能为空"),
	COUPON_SCOPE_TYPE_STORE_ERROR(510001, "当前关联范围类型为部分店铺分类时，范围关联的id不能为空"),
	COUPON_SCOPE_ERROR(510001, "指定商品范围关联id无效！"),
	COUPON_MEMBER_NOT_EXIST(510001, "没有当前会员优惠券"),
	COUPON_MEMBER_STATUS_ERROR(510001, "当前会员优惠券已过期/作废无法变更状态！"),

	//********************************************* 拼团模块异常 *******************************************************
	PINTUAN_MANUAL_OPEN_SUCCESS(511001, "手动开启拼团活动成功"),
	PINTUAN_MANUAL_CLOSE_SUCCESS(511001, "手动关闭拼团活动成功"),
	PINTUAN_ADD_SUCCESS(511001, "添加拼团活动成功"),
	PINTUAN_EDIT_SUCCESS(511001, "修改拼团活动成功"),
	PINTUAN_DELETE_SUCCESS(511001, "删除拼团活动成功"),
	PINTUAN_MANUAL_OPEN_ERROR(511001, "手动开启拼团活动失败"),
	PINTUAN_MANUAL_CLOSE_ERROR(511001, "手动关闭拼团活动失败"),
	PINTUAN_ADD_ERROR(511001, "添加拼团活动失败"),
	PINTUAN_EDIT_ERROR(511001, "修改拼团活动失败"),
	PINTUAN_EDIT_ERROR_ITS_OPEN(511001, "拼团活动已开启，无法修改拼团活动！"),
	PINTUAN_DELETE_ERROR(511001, "删除拼团活动失败"),
	PINTUAN_JOIN_ERROR(511001, "不能参与自己发起的拼团活动！"),
	PINTUAN_LIMIT_NUM_ERROR(511001, "购买数量超过拼团活动限制数量！"),
	PINTUAN_NOT_EXIST_ERROR(511001, "当前拼团活动不存在！"),
	PINTUAN_GOODS_NOT_EXIST_ERROR(511001, "当前拼团商品不存在！"),

	//********************************************* 活动模块异常 *******************************************************
	FULL_DISCOUNT_EDIT_SUCCESS(512001, "修改满优惠活动成功"),
	FULL_DISCOUNT_EDIT_DELETE(512001, "删除满优惠活动成功"),
	FULL_DISCOUNT_MODIFY_ERROR(512001, "当前编辑的满优惠活动已经开始或者已经结束，无法修改"),
	FULL_DISCOUNT_NOT_EXIST_ERROR(512001, "当前要操作的满优惠活动不存在！"),
	FULL_DISCOUNT_WAY_ERROR(512001, "请选择一种优惠方式！"),
	FULL_DISCOUNT_GIFT_ERROR(512001, "请选择赠品！"),
	FULL_DISCOUNT_COUPON_TIME_ERROR(512001, "赠送的优惠券有效时间必须在活动时间之内"),
	FULL_DISCOUNT_MONEY_ERROR(512001, "请填写满减金额"),
	FULL_DISCOUNT_MONEY_GREATER_THAN_MINUS(512001, "满减金额不能大于优惠门槛"),
	FULL_RATE_NUM_ERROR(512001, "请填写打折数值"),
	//直播
	STODIO_GOODS_EXIST_ERROR(512001, "直播商品已存在"),
	COMMODITY_ERROR(512001, "添加直播商品失败"),
	//秒杀
	SECKILL_NOT_START_ERROR(512001, "今日没有限时抢购活动，请明天再来看看吧。"),
	SECKILL_NOT_EXIST_ERROR(512001, "当前参与的秒杀活动不存在！"),
	SECKILL_APPLY_NOT_EXIST_ERROR(512001, "当前参与的秒杀活动不存在！"),
	SECKILL_UPDATE_ERROR(512001, "当前秒杀活动活动已经开始，无法修改！"),
	SECKILL_PRICE_ERROR(512001, "活动价格不能大于商品原价"),
	SECKILL_TIME_ERROR(512001, "时刻参数异常"),
	SECKILL_DELETE_ERROR(512001, "该秒杀活动活动的状态不能删除"),
	SECKILL_OPEN_ERROR(512001, "该秒杀活动活动的状态不能删除"),
	SECKILL_CLOSE_ERROR(512001, "该秒杀活动活动的状态不能关闭"),
	//优惠券
	COUPON_ACTIVITY_START_TIME_ERROR(512001, "活动时间小于当前时间，不能进行编辑删除操作"),
	COUPON_ACTIVITY_MEMBER_ERROR(512001, "指定精准发券则必须指定会员，会员不可以为空"),
	COUPON_ACTIVITY_ITEM_ERROR(512001, "优惠券活动必须指定优惠券，不能为空"),
	COUPON_ACTIVITY_ITEM_MUST_NUM_ERROR(512001, "优惠券活动最多指定10个优惠券"),
	COUPON_ACTIVITY_ITEM_NUM_ERROR(512001, "赠券数量必须大于0"),
	//其他促销
	MEMBER_SIGN_REPEAT(512001, "请勿重复签到"),
	POINT_GOODS_ACTIVE_STOCK_ERROR(512001, "活动库存数量不能高于商品库存"),
	POINT_GOODS_ACTIVE_STOCK_INSUFFICIENT(512001, "积分商品库存不足"),
	//砍价活动
	KANJIA_GOODS_ACTIVE_STOCK_ERROR(512001, "活动库存数量不能高于商品库存"),
	KANJIA_GOODS_ACTIVE_PRICE_ERROR(512001, "最低购买金额不能高于商品金额"),
	KANJIA_GOODS_ACTIVE_HIGHEST_PRICE_ERROR(512001, "最高砍价金额不能为0且不能超过商品金额"),
	KANJIA_GOODS_ACTIVE_LOWEST_PRICE_ERROR(512001, "最低砍价金额不能为0且不能超过商品金额"),
	KANJIA_GOODS_ACTIVE_HIGHEST_LOWEST_PRICE_ERROR(512001, "最低砍价金额不能高于最高砍价金额"),
	KANJIA_GOODS_ACTIVE_SETTLEMENT_PRICE_ERROR(512001, "结算金额不能高于商品金额"),
	KANJIA_GOODS_DELETE_ERROR(512001, "删除砍价商品异常"),
	KANJIA_GOODS_UPDATE_ERROR(512001, "更新砍价商品异常"),
	KANJIA_ACTIVITY_NOT_FOUND_ERROR(512001, "砍价记录不存在"),
	KANJIA_ACTIVITY_LOG_MEMBER_ERROR(512001, "当前会员已经帮砍"),
	KANJIA_ACTIVITY_MEMBER_ERROR(512001, "当前会员已经发起此砍价商品活动"),
	KANJIA_ACTIVITY_NOT_PASS_ERROR(512001, "当前砍价未满足条件，不能进行购买"),
	KANJIA_NUM_BUY_ERROR(512001, "砍价商品购买数量不正确"),

	//********************************************* 店铺模块异常 *******************************************************
	STORE_NOT_EXIST(513001, "此店铺不存在"),
	STORE_NAME_EXIST_ERROR(513001, "店铺名称已存在!"),
	STORE_APPLY_DOUBLE_ERROR(513001, "已有店铺，无需重复申请!"),
	STORE_NOT_OPEN(513001, "该会员未开通店铺"),
	STORE_NOT_LOGIN_ERROR(513001, "未登录店铺"),
	STORE_CLOSE_ERROR(513001, "店铺关闭，请联系管理员"),
	FREIGHT_TEMPLATE_NOT_EXIST(513001, "当前模版不存在"),
	//结算单
	BILL_CHECK_ERROR(513001, "只有已出账结算单可以核对"),
	BILL_COMPLETE_ERROR(513001, "只有已审核结算单可以支付"),
	//文章
	ARTICLE_CATEGORY_NAME_EXIST(513001, "文章分类名称已存在"),
	ARTICLE_CATEGORY_PARENT_NOT_EXIST(513001, "文章分类父分类不存在"),
	ARTICLE_CATEGORY_BEYOND_TWO(513001, "最多为二级分类,操作失败"),
	ARTICLE_CATEGORY_DELETE_ERROR(513001, "该文章分类下存在子分类，不能删除"),
	ARTICLE_CATEGORY_HAS_ARTICLE(513001, "该文章分类下存在文章，不能删除"),
	ARTICLE_CATEGORY_NO_DELETION(513001, "默认文章分类不能进行删除"),
	ARTICLE_NO_DELETION(513001, "默认文章不能进行删除"),
	//页面
	PAGE_NOT_EXIST(513001, "页面不存在"),
	PAGE_OPEN_DELETE_ERROR(513001, "当前页面为开启状态，无法删除"),
	PAGE_DELETE_ERROR(513001, "当前页面为唯一页面，无法删除"),
	PAGE_RELEASE_ERROR(513001, "页面已发布，无需重复提交"),

	//********************************************* 消息模块异常 *******************************************************
	//站内信
	NOTICE_NOT_EXIST(514001, "当前消息模板不存在"),
	NOTICE_ERROR(514001, "修改站内信异常，请稍后重试"),
	NOTICE_SEND_ERROR(514001, "发送站内信异常，请检查系统日志");

	/**
	 * 返回码
	 */
	private final int code;

	/**
	 * 描述
	 */
	private final String desc;

	ResultEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	/**
	 * 根据返回码得到描述信息
	 *
	 * @param code code
	 * @return java.lang.String
	 * @since 2021/2/25 15:48
	 */
	public static String getMessageByCode(int code) {
		for (ResultEnum result : ResultEnum.values()) {
			if (result.getCode() == code) {
				return result.getDesc();
			}
		}
		return null;
	}

	@Override
	public String getNameByCode(int code) {
		for (ResultEnum result : ResultEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}

}
