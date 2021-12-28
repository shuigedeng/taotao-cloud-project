package com.taotao.cloud.stock.api.common.util;

/**
 * 公共常量
 *
 * @author haoxin
 * @date 2021-01-25
 **/
public interface CommonConstant {

    /**
     * 多租户 请求头
     */
    String TENANT_ID = "tenant_id";

    /**
     * 当前页码
     */
    String PAGE = "page";

    /**
     * 每页显示记录数
     */
    String LIMIT = "limit";

    /**
     * 排序字段
     */
    String ORDER_FIELD = "sidx";

    /**
     * 排序方式
     */
    String ORDER = "order";

    /**
     *  升序
     */
    String ASC = "asc";

    /**
     * 手机验证码
     */
    String REDIS_PHONE_CODE = "PC";

}
