//package com.taotao.cloud.data.mybatis.plus.controller;
//
//
//import com.taotao.cloud.data.mybatis.plus.service.ISuperService;
//
///**
// * 基础接口
// *
// * @param <Entity> 实体
// * @author zuihou
// * @date 2020年03月07日21:56:32
// */
//public interface BaseController<Entity> {
//
//	/**
//	 * 获取实体的类型
//	 *
//	 * @return 实体的类型
//	 */
//	Class<Entity> getEntityClass();
//
//	/**
//	 * 获取Service
//	 *
//	 * @return Service
//	 */
//	ISuperService<Entity> getBaseService();
//
////	/**
////	 * 成功返回
////	 *
////	 * @param data 返回内容
////	 * @param <T>  返回类型
////	 * @return R 成功
////	 */
////	default <T> Result<T> success(T data) {
////		return Result.success(data);
////	}
////
////	/**
////	 * 成功返回
////	 *
////	 * @return R.true
////	 */
////	default Result<Boolean> success() {
////		return Result.success(true);
////	}
////
////	/**
////	 * 失败返回
////	 *
////	 * @param msg 失败消息
////	 * @param <T> 返回类型
////	 * @return 失败
////	 */
////	default <T> Result<T> fail(String msg) {
////		return Result.fa(msg);
////	}
////
////	/**
////	 * 失败返回
////	 *
////	 * @param msg  失败消息
////	 * @param args 动态参数
////	 * @param <T>  返回类型
////	 * @return 失败
////	 */
////	default <T> R<T> fail(String msg, Object... args) {
////		return R.fail(msg, args);
////	}
////
////	/**
////	 * 失败返回
////	 *
////	 * @param code 失败编码
////	 * @param msg  失败消息
////	 * @param <T>  返回类型
////	 * @return 失败
////	 */
////	default <T> R<T> fail(int code, String msg) {
////		return R.fail(code, msg);
////	}
////
////	/**
////	 * 失败返回
////	 *
////	 * @param exceptionCode 失败异常码
////	 * @return 失败
////	 */
////	default <T> R<T> fail(BaseExceptionCode exceptionCode) {
////		return R.fail(exceptionCode);
////	}
////
////	/**
////	 * 失败返回
////	 *
////	 * @param exception 异常
////	 * @return 失败
////	 */
////	default <T> R<T> fail(BizException exception) {
////		return R.fail(exception);
////	}
////
////	/**
////	 * 失败返回
////	 *
////	 * @param throwable 异常
////	 * @return 失败
////	 */
////	default <T> R<T> fail(Throwable throwable) {
////		return R.fail(throwable);
////	}
////
////	/**
////	 * 参数校验失败返回
////	 *
////	 * @param msg 错误消息
////	 * @return 失败
////	 */
////	default <T> R<T> validFail(String msg) {
////		return R.validFail(msg);
////	}
////
////	/**
////	 * 参数校验失败返回
////	 *
////	 * @param msg  错误消息
////	 * @param args 错误参数
////	 * @return 失败
////	 */
////	default <T> R<T> validFail(String msg, Object... args) {
////		return R.validFail(msg, args);
////	}
////
////	/**
////	 * 参数校验失败返回
////	 *
////	 * @param exceptionCode 错误编码
////	 * @return 失败
////	 */
////	default <T> R<T> validFail(BaseExceptionCode exceptionCode) {
////		return R.validFail(exceptionCode);
////	}
////
////	/**
////	 * 获取当前id
////	 *
////	 * @return userId
////	 */
////	default Long getUserId() {
////		return ContextUtil.getUserId();
////	}
////
////	/**
////	 * 当前请求租户
////	 *
////	 * @return 租户编码
////	 */
////	default String getTenant() {
////		return ContextUtil.getTenant();
////	}
////
////	/**
////	 * 登录人账号
////	 *
////	 * @return 账号
////	 */
////	default String getAccount() {
////		return ContextUtil.getAccount();
////	}
////
////	/**
////	 * 登录人姓名
////	 *
////	 * @return 姓名
////	 */
////	default String getName() {
////		return ContextUtil.getName();
////	}
//
//}
