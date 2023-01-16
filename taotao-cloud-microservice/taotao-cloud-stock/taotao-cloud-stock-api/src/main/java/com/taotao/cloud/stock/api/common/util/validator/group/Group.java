package com.taotao.cloud.stock.api.common.util.validator.group;

import jakarta.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author shuigedeng
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
