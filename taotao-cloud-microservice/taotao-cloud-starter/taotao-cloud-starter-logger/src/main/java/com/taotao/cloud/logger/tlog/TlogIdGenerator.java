package com.taotao.cloud.logger.tlog;

import com.taotao.cloud.common.utils.common.IdGeneratorUtil;
import com.yomahub.tlog.id.TLogIdGenerator;

/**
 * tlog身份证生成器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-23 11:29:45
 */
public class TlogIdGenerator extends TLogIdGenerator {
    @Override
    public String generateTraceId() {
        return IdGeneratorUtil.getIdStr();
    }
}
