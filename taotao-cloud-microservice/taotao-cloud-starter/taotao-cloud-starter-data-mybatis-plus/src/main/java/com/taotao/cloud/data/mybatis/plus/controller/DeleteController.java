//package com.taotao.cloud.data.mybatis.plus.controller;
//
//import com.taotao.cloud.common.model.Result;
//import java.io.Serializable;
//import java.util.List;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//
///**
// * 删除Controller
// *
// * @param <Entity> 实体
// * @param <Id>     主键
// * @author zuihou
// * @date 2020年03月07日22:02:16
// */
//public interface DeleteController<Entity, Id extends Serializable> extends BaseController<Entity> {
//
//	/**
//	 * 删除方法
//	 *
//	 * @param ids id
//	 * @return 是否成功
//	 */
//	@DeleteMapping
//	default Result<Boolean> delete(@RequestBody List<Id> ids) {
//		getBaseService().removeByIds(ids);
//		return Result.success(true);
//	}
//
////    /**
////     * 自定义删除
////     *
////     * @param ids id
////     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
////     */
////    default R<Boolean> handlerDelete(List<Id> ids) {
////        return R.successDef(true);
////    }
//
//}
