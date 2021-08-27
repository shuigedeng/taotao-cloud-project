package com.taotao.cloud.health.export;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Context;
import com.yh.csx.bsf.core.base.BsfEnvironmentEnum;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.health.base.AbstractExport;
import com.yh.csx.bsf.health.base.Report;
import com.yh.csx.bsf.health.config.ExportProperties;
import com.yh.csx.bsf.health.config.HealthProperties;
import lombok.val;
import lombok.var;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.marker.MapEntriesAppendingMarker;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: chejiangyi
 * @version: 2019-08-13 19:48
 **/
public class ElkExport extends AbstractExport {

    private LogstashTcpSocketAppender appender;


    @Override
    public void start() {
        super.start();
        val log =LoggerFactory.getILoggerFactory();
        if(log!=null&&log instanceof Context) {
            appender = new LogstashTcpSocketAppender();
            appender.setContext((Context) log);
            var destinations = ExportProperties.Default().getBsfElkDestinations();
            if (destinations == null || destinations.length == 0) {
                destinations = new String[]{PropertyUtils.getPropertyCache(BsfEnvironmentEnum.ELK_DEV.getServerkey(), "")};
            }
            for (String destination : destinations) {
                appender.addDestination(destination);
            }
            val encoder = new LogstashEncoder();
            val appname = "bsfReport-" + PropertyUtils.getPropertyCache(HealthProperties.SpringApplictionName, "");
            encoder.setCustomFields("{\"appname\":\"" + appname + "\",\"appindex\":\"bsfReport\"}");
            encoder.setEncoding("UTF-8");
            appender.setEncoder(encoder);
            appender.start();
        }
    }
    @Override
    public void run(Report report){
        if(appender == null||!ExportProperties.Default().isBsfElkEnabled())
        {
            return;
        }
        Map<String,Object> map= new LinkedHashMap();
        report.eachReport((String field, Report.ReportItem reportItem) -> {
            if(reportItem!=null && reportItem.getValue() instanceof Number) {
                map.put(field.replace(".", "_"), reportItem.getValue());
            }
            return reportItem;
        });
        val event = createLoggerEvent(map,"bsf Report:"+ DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        appender.doAppend(event);
    }

    private LoggingEvent createLoggerEvent(Map<String, Object> values, String message){
        LoggingEvent loggingEvent = new LoggingEvent();
        loggingEvent.setTimeStamp(System.currentTimeMillis());
        loggingEvent.setLevel(Level.INFO);
        loggingEvent.setLoggerName("bsfReportLogger");
        loggingEvent.setMarker(new MapEntriesAppendingMarker(values));
        loggingEvent.setMessage(message);
        loggingEvent.setArgumentArray(new String[0]);
        loggingEvent.setThreadName(Thread.currentThread().getName());
        return loggingEvent;
    }

    @Override
    public void close() {
        super.close();
        if(appender!=null) {
            appender.stop();
        }
    }
}
