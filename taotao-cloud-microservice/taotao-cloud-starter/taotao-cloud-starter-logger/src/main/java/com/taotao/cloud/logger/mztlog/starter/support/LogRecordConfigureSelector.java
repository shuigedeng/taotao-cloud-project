package com.taotao.cloud.logger.mztlog.starter.support;

import com.taotao.cloud.logger.mztlog.starter.annotation.EnableLogRecord;
import com.taotao.cloud.logger.mztlog.starter.configuration.LogRecordProxyAutoConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.lang.Nullable;

public class LogRecordConfigureSelector extends AdviceModeImportSelector<EnableLogRecord> {
    private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME =
            "com.mzt.logapi.starter.configuration.LogRecordProxyAutoConfiguration";


    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(), LogRecordProxyAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME};
            default:
                return null;
        }
    }
}
