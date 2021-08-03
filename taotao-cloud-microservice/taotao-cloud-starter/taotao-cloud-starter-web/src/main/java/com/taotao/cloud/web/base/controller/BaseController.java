package com.taotao.cloud.web.base.controller;


import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.web.base.service.SuperService;

/**
 * 基础接口
 *
 * @param <Entity> 实体
 * @author zuihou
 * @date 2020年03月07日21:56:32
 */
public interface BaseController<Entity> {

	/**
	 * 获取实体的类型
	 *
	 * @return 实体的类型
	 */
	Class<Entity> getEntityClass();

	/**
	 * 获取Service
	 *
	 * @return Service
	 */
	SuperService<Entity> getBaseService();

	/**
	 * 成功返回
	 *
	 * @param data 返回内容
	 * @param <T>  返回类型
	 * @return R 成功
	 */
	default <T> Result<T> success(T data) {
		return Result.success(data);
	}

	/**
	 * 成功返回
	 *
	 * @return R.true
	 */
	default Result<Boolean> success() {
		return Result.success();
	}

	/**
	 * 失败返回
	 *
	 * @param msg 失败消息
	 * @return 失败
	 */
	default Result<String> fail(String msg) {
		return Result.fail(msg);
	}


	/**
	 * 失败返回
	 *
	 * @param code 失败编码
	 * @param msg  失败消息
	 * @return 失败
	 */
	default Result<String> fail(int code, String msg) {
		return Result.fail(msg, code);
	}

	/**
	 * 失败返回
	 *
	 * @param exception 异常
	 * @return 失败
	 */
	default <T> Result<T> fail(BusinessException exception) {
		return Result.fail(exception);
	}

	/**
	 * 失败返回
	 *
	 * @param throwable 异常
	 * @return 失败
	 */
	default <T> Result<T> fail(Throwable throwable) {
		return Result.fail(throwable);
	}

	/**
	 * 参数校验失败返回
	 *
	 * @param msg 错误消息
	 * @return 失败
	 */
	default <T> Result<T> validFail(String msg) {
		return Result.validFail(msg);
	}

	/**
	 * 参数校验失败返回
	 *
	 * @param msg  错误消息
	 * @param args 错误参数
	 * @return 失败
	 */
	default <T> Result<T> validFail(String msg, Object... args) {
		return Result.validFail(msg, args);
	}

	/**
	 * 参数校验失败返回
	 *
	 * @param resultEnum 错误编码
	 * @return 失败
	 */
	default <T> Result<T> validFail(ResultEnum resultEnum) {
		return Result.validFail(resultEnum);
	}

	/**
	 * 获取当前id
	 *
	 * @return userId
	 */
	default Long getUserId() {
		return SecurityUtil.getUserId();
	}

	/**
	 * 当前请求租户
	 *
	 * @return 租户编码
	 */
	default String getTenant() {
		return SecurityUtil.getTenant();
	}

	/**
	 * 登录人账号
	 *
	 * @return 账号
	 */
	default String getAccount() {
		return SecurityUtil.getUser().getNickname();
	}

	/**
	 * 登录人姓名
	 *
	 * @return 姓名
	 */
	default String getName() {
		return SecurityUtil.getUsername();
	}
}
