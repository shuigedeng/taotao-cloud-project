package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.ConvertUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import com.yh.csx.bsf.health.utils.ProcessUtils;
import lombok.Data;

import static com.yh.csx.bsf.health.utils.ProcessUtils.getProcessID;

public class NetworkCollectTask extends AbstractCollectTask {

    public NetworkCollectTask() {

    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.network.timeSpan",10);
    }

    @Override
    public String getDesc() {
        return "network采集";
    }

    @Override
    public String getName() {
        return "network.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.network.enabled",true);
    }


    @Override
    protected Object getData() {
        NetworkInfo ioInfo = new NetworkInfo();
        ioInfo.processSysTcpListenNum = ConvertUtils.convert(ProcessUtils.execCmd("netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'LISTEN' |wc -l"),Long.class);
        ioInfo.processSysTcpEstablishedNum = ConvertUtils.convert(ProcessUtils.execCmd("netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'ESTABLISHED' |wc -l"),Long.class);
        ioInfo.processSysTcpTimeWaitNum = ConvertUtils.convert(ProcessUtils.execCmd("netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1 |egrep -w 'TIME_WAIT' |wc -l"),Long.class);
        ioInfo.processTcpListenNum = ConvertUtils.convert(ProcessUtils.execCmd("netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'LISTEN' |wc -l".replaceAll("\\$PID",getProcessID())),Long.class);
        ioInfo.processTcpEstablishedNum = ConvertUtils.convert(ProcessUtils.execCmd("netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'ESTABLISHED' |wc -l".replaceAll("\\$PID",getProcessID())),Long.class);
        ioInfo.processTcpTimeWaitNum = ConvertUtils.convert(ProcessUtils.execCmd("netstat -anp |awk '/^tcp/ {print $6,$7}' |cut -d/ -f1  |egrep -w '$PID' |egrep -w 'TIME_WAIT' |wc -l".replaceAll("\\$PID",getProcessID())),Long.class);
        return ioInfo;
    }


    @Data
    private static class NetworkInfo {
        @FieldReport(name = "network.process.tcp.listen.number",desc="当前进程TCP LISTEN状态连接数")
        private long processTcpListenNum;
        @FieldReport(name = "network.process.tcp.established.number",desc="当前进程TCP ESTABLISHED状态连接数")
        private long processTcpEstablishedNum;
        @FieldReport(name = "network.process.tcp.time_wait.number",desc="当前进程TCP TIME_WAIT连接数")
        private long processTcpTimeWaitNum;
        @FieldReport(name = "network.sys.tcp.listen.number",desc="系统TCP LISTEN状态连接数")
        private long processSysTcpListenNum;
        @FieldReport(name = "network.sys.tcp.established.number",desc="系统TCP ESTABLISHED状态连接数")
        private long processSysTcpEstablishedNum;
        @FieldReport(name = "network.sys.tcp.time_wait.number",desc="系统TCP TIME_WAIT连接数")
        private long processSysTcpTimeWaitNum;
    }



}
