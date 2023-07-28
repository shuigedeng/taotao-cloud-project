package com.taotao.cloud.auth.biz.uaa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportMessage;
import org.springframework.boot.logging.LogLevel;

import java.util.function.Supplier;

class ConditionEvaluationReportLogger {

    private final Log logger = LogFactory.getLog(getClass());

    private final Supplier<ConditionEvaluationReport> reportSupplier;

    private final LogLevel logLevel;

    ConditionEvaluationReportLogger(LogLevel logLevel, Supplier<ConditionEvaluationReport> reportSupplier) {
//        Assert.isTrue(isInfoOrDebug(logLevel), "LogLevel must be INFO or DEBUG");
        this.logLevel = logLevel;
        this.reportSupplier = reportSupplier;
    }

    private boolean isInfoOrDebug(LogLevel logLevelForReport) {
        return LogLevel.INFO.equals(logLevelForReport) || LogLevel.DEBUG.equals(logLevelForReport);
    }

    void logReport(boolean isCrashReport) {
        ConditionEvaluationReport report = this.reportSupplier.get();
        if (report == null) {
            this.logger.info("Unable to provide the condition evaluation report");
            return;
        }
        if (!report.getConditionAndOutcomesBySource().isEmpty()) {
            if (this.logLevel.equals(LogLevel.INFO)) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info(new ConditionEvaluationReportMessage(report));
                } else if (isCrashReport) {
                    logMessage("info");
                }
            } else {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(new ConditionEvaluationReportMessage(report));
                } else if (isCrashReport) {
                    logMessage("debug");
                }
            }
        }
    }

    private void logMessage(String logLevel) {
        this.logger.info(String.format("%n%nError starting ApplicationContext. To display the "
                + "condition evaluation report re-run your application with '" + logLevel + "' enabled."));
    }

}
