package com.taotao.cloud.data.mybatis.plus.handler;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**   
* Jackson 实现 JSON 字段类型处理器, 会记录对象属性类型, 通常用于被容器(List、Set、Map)包装的属性上
 *
 * <pre class="code">
 *      支付通道信息列表
 *      @see PayChannelInfo
 *
 *      @TableField(typeHandler = JacksonListTypeHandler.class)
 *      @BigField
 *      private List<PayChannelInfo> payChannelInfo;
 * </pre>
*/
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JacksonListTypeHandler extends AbstractJsonTypeHandler<Object> {
    private final Class<?> type;

    public JacksonListTypeHandler(Class<?> type) {
        if (LogUtils.isTraceEnabled()) {
	        LogUtils.trace("JacksonListTypeHandler(" + type + ")");
        }
        Assert.notNull(type, "Type argument cannot be null");
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        return JsonUtils.toObject(json,type);
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJSONString(obj);
    }
}
