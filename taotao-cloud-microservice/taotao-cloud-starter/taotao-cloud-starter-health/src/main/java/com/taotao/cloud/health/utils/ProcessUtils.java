package com.taotao.cloud.health.utils;

import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.health.config.HealthProperties;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

public class ProcessUtils {

    public static String execCmd(String cmd){
        if(isWinOs()){
            return "-1";
        } else {
            try{
                String result = ProcessUtils.execCmd(cmd,null);
                return result;
            }catch (Exception e){
                return "-2";
            }
        }
    }

    public static  boolean isWinOs(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return true;
        }
        return false;
    }

    public static  String getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getName().split("@")[0];
    }

    /**
     * 执行系统命令, 返回执行结果
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) {
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        try {
            String[] commond = {"sh","-c",cmd};
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(commond, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor(3, TimeUnit.SECONDS);

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }

            return result.toString();

        }catch (Exception e){
            LogUtils.error(ProcessBuilder.class, HealthProperties.Project,"execCmd" , e);
        }finally {
            closeStream(bufrIn);
            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        
        return "-3";
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
            }
        }
    }
}

