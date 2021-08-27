package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;

import com.sun.management.OperatingSystemMXBean;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 19:07
 **/
public class MemeryCollectTask extends AbstractCollectTask {

    OperatingSystemMXBean systemMXBean;
    public MemeryCollectTask(){
        systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.memery.timeSpan",10);
    }

    @Override
    public String getDesc() {
        return "内存监测";
    }

    @Override
    public String getName() {
        return "memery.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.memery.enabled",true);
    }


    @Override
    protected Object getData() {
        JvmInfo jvmInfo = new JvmInfo();

        Runtime rt = Runtime.getRuntime();
        jvmInfo.setTotalInfo(new JvmTotalInfo());
        val totalInfo = jvmInfo.getTotalInfo();
        totalInfo.setTotal(rt.totalMemory() / byteToMb);
        totalInfo.setFree(rt.freeMemory() / byteToMb);
        totalInfo.setMax(rt.maxMemory() / byteToMb);
        totalInfo.setUse(totalInfo.getTotal() - totalInfo.getFree());

        val pools = ManagementFactory.getMemoryPoolMXBeans();
        if(pools != null && !pools.isEmpty()){
            for(MemoryPoolMXBean pool : pools){
                String name = pool.getName();
                Object jvmGen = null;
                if(name.contains("Eden"))
                {
                    jvmInfo.setEdenInfo(new JVMEdenInfo());
                    jvmGen=jvmInfo.getEdenInfo();
                }
                else if(name.contains("Code Cache")){
                    jvmInfo.setGenCodeCache(new JVMCodeCacheInfo());
                    jvmGen = jvmInfo.getGenCodeCache();
                }
                else if(name.contains("Old")){
                    jvmInfo.setGenOldInfo(new JVMOldInfo());
                    jvmGen = jvmInfo.getGenOldInfo();
                }
                else if(name.contains("Perm")){
                    jvmInfo.setGenPermInfo(new JVMPermInfo());
                    jvmGen = jvmInfo.getGenPermInfo();
                }
                else if(name.contains("Survivor")){
                    jvmInfo.setSurvivorInfo(new JVMSurvivorInfo());
                    jvmGen = jvmInfo.getSurvivorInfo();
                }
                else if(name.contains("Metaspace")){
                    jvmInfo.setGenMetaspace(new JVMMetaspaceInfo());
                    jvmGen = jvmInfo.getGenMetaspace();
                }
                else if(name.contains("Compressed Class Space")){
                    jvmInfo.setGenCompressedClassSpace(new JVMCompressedClassSpaceInfo());
                    jvmGen = jvmInfo.getGenCompressedClassSpace();
                }
                else
                {
                   // int a=1;
                }
                if(jvmGen!=null && pool.getUsage()!=null){
                    ReflectionUtils.setFieldValue( ReflectionUtils.findField(jvmGen.getClass(),"init"),jvmGen,pool.getUsage().getInit()/byteToMb);
                    ReflectionUtils.setFieldValue( ReflectionUtils.findField(jvmGen.getClass(),"used"),jvmGen,pool.getUsage().getUsed()/byteToMb);
                    ReflectionUtils.setFieldValue( ReflectionUtils.findField(jvmGen.getClass(),"max"),jvmGen,pool.getUsage().getMax()/byteToMb);
                    long poolUsageCommitted=pool.getUsage().getCommitted();
                    ReflectionUtils.setFieldValue( ReflectionUtils.findField(jvmGen.getClass(),"committed"),jvmGen,poolUsageCommitted/byteToMb);
                    if(poolUsageCommitted>0){
                        ReflectionUtils.setFieldValue( ReflectionUtils.findField(jvmGen.getClass(),"usedRate"),jvmGen,(pool.getUsage().getUsed()*100/poolUsageCommitted));
                    }
                }
            }
        }

        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setFree(systemMXBean.getFreePhysicalMemorySize() / byteToMb);
        systemInfo.setTotal(systemMXBean.getTotalPhysicalMemorySize() / byteToMb);
        systemInfo.setUse(systemInfo.getTotal() - systemInfo.getFree());

        return new MemeryInfo(jvmInfo,systemInfo);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JvmInfo{
        @FieldReport(name="memery.jvm.total.info",desc = "JVM 内存统计")
        private JvmTotalInfo totalInfo;
        @FieldReport(name = "memery.jvm.eden.info",desc="JVM Eden 年轻代内存(M)")
        private JVMEdenInfo edenInfo;
        @FieldReport(name = "memery.jvm.survivor.info",desc="JVM Survivor 年轻代内存(M)")
        private JVMSurvivorInfo survivorInfo;
        @FieldReport(name = "memery.jvm.old.info",desc="JVM Old 老年代内存(M)")
        private JVMOldInfo genOldInfo;
        @FieldReport(name = "memery.jvm.perm.info",desc="JVM Perm 永久代内存(M)")
        private JVMPermInfo genPermInfo;
        @FieldReport(name = "memery.jvm.codeCache.info",desc="JVM CodeCache 编译码缓存内存(M)")
        private JVMCodeCacheInfo genCodeCache;
        @FieldReport(name = "memery.jvm.metaspace.info",desc="JVM metaspace 元数据缓存内存(M)")
        private JVMMetaspaceInfo genMetaspace;
        @FieldReport(name = "memery.jvm.compressedClassSpace.info",desc="JVM CompressedClassSpace 缓存内存(M)")
        private JVMCompressedClassSpaceInfo genCompressedClassSpace;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JvmTotalInfo
    {
        @FieldReport(name = "memery.jvm.use",desc="JVM内存已用空间(M)")
        private double use;
        @FieldReport(name = "memery.jvm.free",desc="JVM内存可用空间(M)")
        private double free;
        @FieldReport(name = "memery.jvm.max",desc="JVM内存最大可用空间(M)")
        private double max;
        @FieldReport(name = "memery.jvm.total",desc="JVM内存总空间(M)")
        private double total;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SystemInfo{
        @FieldReport(name = "memery.os.use",desc="Os内存已用空间(M)")
        private double use;
        @FieldReport(name = "memery.os.free",desc="Os内存可用空间(M)")
        private double free;
        @FieldReport(name = "memery.os.total",desc="Os内存总空间(M)")
        private double total;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MemeryInfo{
        @FieldReport(name = "memery.jvm",desc="JVM内存空间(M)")
        private JvmInfo jvmInfo=new JvmInfo();
        @FieldReport(name = "memery.system",desc="Os内存空间(M)")
        private SystemInfo systemInfo = new SystemInfo();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMPermInfo{
        @FieldReport(name = "memery.jvm.gen.perm.init",desc="perm 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.perm.max",desc="perm 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.perm.used",desc="perm 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.perm.committed",desc="perm 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.perm.usedRate",desc="perm 使用率 %")
        private double usedRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMOldInfo{
        @FieldReport(name = "memery.jvm.gen.old.init",desc="old 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.old.max",desc="old 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.old.used",desc="old 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.old.committed",desc="old 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.old.usedRate",desc="old 使用率 %")
        private double usedRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMSurvivorInfo{
        @FieldReport(name = "memery.jvm.gen.survivor.init",desc="survivor 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.survivor.max",desc="survivor 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.survivor.used",desc="survivor 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.survivor.committed",desc="survivor 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.survivor.usedRate",desc="survivor 使用率 %")
        private double usedRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMEdenInfo{
        @FieldReport(name = "memery.jvm.gen.eden.init",desc="eden 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.eden.max",desc="eden 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.eden.used",desc="eden 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.eden.committed",desc="eden 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.eden.usedRate",desc="eden 使用率 %")
        private double usedRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMCodeCacheInfo{
        @FieldReport(name = "memery.jvm.gen.codeCache.init",desc="codeCache 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.codeCache.max",desc="codeCache 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.codeCache.used",desc="codeCache 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.codeCache.committed",desc="codeCache 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.codeCache.usedRate",desc="codeCache 使用率 %")
        private double usedRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMMetaspaceInfo{
        @FieldReport(name = "memery.jvm.gen.metaspace.init",desc="metaspace 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.metaspace.max",desc="metaspace 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.metaspace.used",desc="metaspace 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.metaspace.committed",desc="metaspace 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.metaspace.usedRate",desc="metaspace 使用率 %")
        private double usedRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class JVMCompressedClassSpaceInfo{
        @FieldReport(name = "memery.jvm.gen.compressedClassSpace.init",desc="Compressed Class Space 初始内存大小(M)")
        private double init;
        @FieldReport(name = "memery.jvm.gen.compressedClassSpace.max",desc="Compressed Class Space 最大内存大小(M)")
        private double max;
        @FieldReport(name = "memery.jvm.gen.compressedClassSpace.used",desc="Compressed Class Space 已使用内存大小(M)")
        private double used;
        @FieldReport(name = "memery.jvm.gen.compressedClassSpace.committed",desc="Compressed Class Space 已申请内存大小(M)")
        private double committed;
        @FieldReport(name = "memery.jvm.gen.compressedClassSpace.usedRate",desc="Compressed Class Space 使用率 %")
        private double usedRate;
    }
}
