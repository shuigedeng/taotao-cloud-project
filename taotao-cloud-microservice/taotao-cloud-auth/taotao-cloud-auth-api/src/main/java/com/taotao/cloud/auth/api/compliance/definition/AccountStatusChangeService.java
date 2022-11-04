//
// package com.taotao.cloud.auth.api.compliance.definition;
//
// import cn.herodotus.engine.web.core.definition.ApplicationStrategyEvent;
// import cn.herodotus.engine.web.core.domain.UserStatus;
//
// /**
//  * <p>Description: 用户状态变更服务 </p>
//  *
//  * @author : gengwei.zheng
//  * @date : 2022/7/10 16:23
//  */
// public interface AccountStatusChangeService extends ApplicationStrategyEvent<UserStatus> {
//
//     /**
//      * Request Mapping 收集汇总的服务名称
//      *
//      * @return 服务名称
//      */
//     String getDestinationServiceName();
//
//     default void process(UserStatus status) {
//         postProcess(getDestinationServiceName(), status);
//     }
//
// }
