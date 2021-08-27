package com.taotao.cloud.health.dump;

import com.yh.csx.bsf.core.base.BsfException;
import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.core.util.WebUtils;
import com.yh.csx.bsf.health.config.HealthProperties;
import com.yh.csx.bsf.health.utils.ProcessUtils;
import lombok.val;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: chejiangyi
 * @version: 2019-09-07 13:36
 **/
public class DumpProvider {
    public File[] getList(){
        File file = new File(".");
        val fs = file.listFiles((dir,name)->{
            if(name.contains(".hprof")) {
                return true;
            }
            else{
                return false;
            }
        });
       return fs;
    }

    public void list(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(("不要多次dump,一分钟仅限一次,仅限linux系统有效<br/><a href='do/'>立即Dump</a><br/>"));
        for(val f:getList()) {
            if(!f.getName().endsWith(".tar")) {
                stringBuilder.append(String.format("%s (%s M)<a href='zip/?name=%s'>压缩</a><br/>", f.getName(),f.length()/1024/1024,f.getName()));
            }else{
                stringBuilder.append(String.format("%s (%s M)<a href='download/?name=%s'>下载</a><br/>", f.getName(),f.length()/1024/1024,f.getName()));
            }
        }
        response(stringBuilder.toString());
    }

    public void zip(String name){
        for(val f:getList()){
            if(name!=null&&name.equals(f.getName())){
                try {
                    Runtime.getRuntime().exec(String.format("tar -zcvf %s.tar %s",
                            name, name));
                    response("压缩成功,请等待耐心等待,不要重复执行!");
                }catch (Exception exp){
                    LogUtils.error(DumpProvider.class, HealthProperties.Project,"zip 出错",exp);
                    response("压缩出错:"+exp.getMessage());
                }
            }
        }
    }

    private static Long lastDumpTime =0L;
    public void dump(){
        try {
            if(System.currentTimeMillis()-lastDumpTime< TimeUnit.MINUTES.toMillis(1))
            {throw new BsfException("dump过于频繁,请等待后1分钟重试");}
            Runtime.getRuntime().exec(String.format("jmap -dump:format=b,file=heap.%s.hprof %s",
                    new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()), ProcessUtils.getProcessID()));
            lastDumpTime=System.currentTimeMillis();
            response("dump成功,请等待耐心等待,不要重复执行!");
        }catch (Exception exp){
            LogUtils.error(DumpProvider.class, HealthProperties.Project,"dump 出错",exp);
            response("dump出错:"+exp.getMessage());
        }
    }

    public void download(String name){
        for(val f:getList()){
            if(name!=null&&name.equals(f.getName())){
                val response = WebUtils.getResponse();
                response.reset();
                response.setContentType("application/x-download");
                response.addHeader("Content-Disposition","attachment;filename="+ f.getName());
                response.addHeader("Content-Length", "" + f.length());
                response.setHeader("Content-type","");
                try {
                    try (val fs = new FileInputStream(f)) {
                        try (InputStream fis = new BufferedInputStream(fs)) {
                            try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
                                response.setContentType("application/octet-stream");
                                byte[] buffer = new byte[1024];
                                int i = -1;
                                while ((i = fis.read(buffer)) != -1) {
                                    out.write(buffer, 0, i);
                                }
                                fis.close();
                                out.flush();
                                out.close();
                            }
                        }
                    }
                }catch (Exception exp){
                    LogUtils.error(DumpProvider.class, HealthProperties.Project,"download 出错",exp);
                    response("下载出错:"+exp.getMessage());
                }
            }
        }
    }

    private void response(String html){
        try {
            val response = WebUtils.getResponse();
            response.reset();
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().append(html);
            response.getWriter().flush();
            response.getWriter().close();
        }catch (Exception e){}
    }
}
