package com.taotao.cloud.data.mybatisplus.datascope.perm.scope;

/**
* 数据范围权限业务实现接口
*/
public interface DataPermScopeHandler {

    /**
     * 返回数据权限查询条件的集合
     */
    DataPermScope getDataPermScope();
}
