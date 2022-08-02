package com.taotao.cloud.sys.biz.modules.core.service.plugin;//package com.sanri.tools.modules.core.service.plugin;
//
//import com.sanri.tools.modules.core.dtos.PluginDto;
//import lombok.Data;
//import org.apache.commons.lang3.time.DateUtils;
//
///**
// * 增强工具属性,统计一些调用属性,使更热门的工具排名更靠前
// * 5 分钟之内根据调用时间进行排序 , 5 分钟之后根据调用次数进行排序
// */
//@Data
//public class EnhancePluginDto implements Comparable<EnhancePluginDto>{
//    /**
//     * 插件信息
//     */
//    private PluginDto pluginDto;
//    /**
//     * 总共调用次数
//     */
//    private int totalCalls;
//    /**
//     * 上次调用时间
//     */
//    private long lastCallTime;
//
//    public static final long FIVE_MINUTES = 5 * 60 * 1000;
//
//    public EnhancePluginDto(PluginDto pluginDto) {
//        this.pluginDto = pluginDto;
//        this.totalCalls = 0 ;
//        this.lastCallTime = System.currentTimeMillis();
//    }
//
//    public EnhancePluginDto(PluginDto pluginDto, PluginManager.SerializerPlugin serializerPlugin) {
//        this.pluginDto = pluginDto;
//        this.totalCalls = serializerPlugin.getTotalCalls();
//        this.lastCallTime = serializerPlugin.getLastCallTime();
//    }
//
//    @Override
//    public int compareTo(EnhancePluginDto o) {
//        long currentTimeMillis = System.currentTimeMillis();
//        boolean otherTimeout = currentTimeMillis - o.lastCallTime > FIVE_MINUTES;
//        boolean thisTimeout = currentTimeMillis - this.lastCallTime > FIVE_MINUTES;
//
//        if(otherTimeout && !thisTimeout){
//            return -1;
//        }
//        if(!otherTimeout && thisTimeout){
//            return 1;
//        }
//        if(!otherTimeout && !thisTimeout){
//            // 5 分钟之内,调用时间会有一定占比, 5 分钟之后只根据调用次数
//            long sub = o.lastCallTime - lastCallTime;
//            return (int) (((double)sub) * 0.01 + ((double)( totalCalls - o.totalCalls )) * 0.5);
//        }
//
//        // 5 分钟之后 , 只根据调用次数排序
//        return o.totalCalls - totalCalls;
//    }
//}
