package com.taotao.cloud.health.utils;

import com.yh.csx.bsf.core.base.BsfExceptionType;
import com.yh.csx.bsf.core.base.BsfLevel;
import com.yh.csx.bsf.core.http.DefaultHttpClient;
import com.yh.csx.bsf.core.http.HttpClient;
import com.yh.csx.bsf.core.thread.ThreadPool;
import com.yh.csx.bsf.core.util.NetworkUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.health.base.EnumWarnType;
import com.yh.csx.bsf.health.base.Message;
import com.yh.csx.bsf.health.config.HealthProperties;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ExceptionUtils {
    private final static String exceptionUrl="bsf.report.exception.url";
    /**
     *@描述 上报异常
     *@参数  [message, applictionName]
     *@返回值  void
     *@创建人  霍钧城
     *@创建时间  2020/12/25
     *@修改历史：
     */
    public static void reportException(Message message,String applictionName){
        if(message.getWarnType()==EnumWarnType.ERROR){
            AtomicReference<String> title= new AtomicReference<>(message.getTitle());
            ThreadPool.System.submit("bsf系统任务:异常上报",()->{
                Map<String,Object> param=new HashMap();
                param.put("exceptionTitle",title.get());
                param.put("exceptionType",message.getExceptionType().getCode());
                param.put("exceptionLevel",message.getLevelType().getLevel());
                if(StringUtils.isNotBlank(message.getExceptionCode())){
                    param.put("exceptionCode",message.getExceptionCode());
                }
                if(StringUtils.isNotBlank(message.getBizScope())){
                    param.put("bizScope",message.getBizScope());
                }
                param.put("exceptionContent",String.format("[%s][%s][%s]%s",
                        NetworkUtils.getIpAddress(),
                        PropertyUtils.getPropertyCache(HealthProperties.BsfEnv,""),
                        com.yh.csx.bsf.core.util.StringUtils.nullToEmpty(PropertyUtils.getPropertyCache(HealthProperties.SpringApplictionName,"")),
                        message.getContent()));
                if(StringUtils.isNotBlank(applictionName)){
                    param.put("projectBeName", applictionName);
                }else{
                    param.put("projectBeName", PropertyUtils.getPropertyCache(HealthProperties.SpringApplictionName,StringUtils.EMPTY));
                }
                HttpClient.Params params = HttpClient.Params.custom().setContentType(ContentType.APPLICATION_JSON).add(param).build();
                DefaultHttpClient.Default.post(PropertyUtils.getPropertyCache(exceptionUrl,StringUtils.EMPTY), params);
            });
        }
    }
    /**
     *@描述 上报异常
     *@参数  [message]
     *@返回值  void
     *@创建人  霍钧城
     *@创建时间  2020/12/30
     *@修改历史：
     */
    public static void reportException(Message message){
        reportException(message,null);
    }
    /**
     *@描述 上报异常
     *@参数  [levelType, title, content]
     *@返回值  void
     *@创建人  霍钧城
     *@创建时间  2020/12/25
     *@修改历史：
     */
    public static void reportException(BsfLevel levelType, String title, String content){
        reportException(new Message(EnumWarnType.ERROR,title,content,levelType, BsfExceptionType.BE,null,null),null);
    }
    /**
     *@描述 上报异常
     *@参数  [levelType, title, content, applictionName]
     *@返回值  void
     *@创建人  霍钧城
     *@创建时间  2020/12/25
     *@修改历史：
     */
    public static void reportException(BsfLevel levelType,String title,String content,String applictionName){
        reportException(new Message(EnumWarnType.ERROR,title,content,levelType,BsfExceptionType.BE,null,null),applictionName);
    }
}

