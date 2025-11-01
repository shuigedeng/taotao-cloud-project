/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.config.aop.execl;

import com.taotao.boot.common.utils.date.DateUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ExcelUploadAspect {

    public static ThreadFactory commonThreadFactory = new ThreadFactoryBuilder()
            .setNamePrefix("upload-pool-%d")
            .setPriority(Thread.NORM_PRIORITY)
            .build();

    public static ExecutorService uploadExecuteService = new ThreadPoolExecutor(
            10,
            20,
            300L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            commonThreadFactory,
            new ThreadPoolExecutor.AbortPolicy());

    @Pointcut("@annotation(com.taotao.cloud.sys.biz.config.aop.execl.ExcelUpload)")
    public void uploadPoint() {}

    @Around(value = "uploadPoint()")
    public Object uploadControl(ProceedingJoinPoint pjp) {
        // 获取方法上的注解，进而获取uploadType
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        ExcelUpload annotation = signature.getMethod().getAnnotation(ExcelUpload.class);
        ExcelUploadType type = annotation == null ? ExcelUploadType.未知 : annotation.type();
        // 获取batchNo
        String batchNo = UUID.randomUUID().toString().replace("-", "");
        // 初始化一条上传的日志，记录开始时间
        // writeLogToDB(batchNo, type, new Date());

        // 线程池启动异步线程，开始执行上传的逻辑，pjp.proceed()就是你实现的上传功能
        uploadExecuteService.submit(() -> {
            try {
                String errorMessage = (String) pjp.proceed();
                // 没有异常直接成功
                if (StrUtil.isEmpty(errorMessage)) {
                    // 成功，写入数据库，具体不展开了
                    // writeSuccessToDB(batchNo);
                } else {
                    // 失败，因为返回了校验信息
                    fail(errorMessage, batchNo);
                }
            } catch (Throwable e) {
                LogUtils.error("导入失败：", e);
                // 失败，抛了异常，需要记录
                fail(e.toString(), batchNo);
            }
        });
        return new Object();
    }

    private void fail(String message, String batchNo) {
        // 生成上传错误日志文件的文件key
        String s3Key = UUID.randomUUID().toString().replace("-", "");
        // 生成文件名称
        String fileName = "错误日志_"
                +
                // DateUtil.format(new Date(), "yyyy年MM月dd日HH时mm分ss秒") +
                // ExportConstant.txtSuffix;
                DateUtils.format(new Date(), "yyyy年MM月dd日HH时mm分ss秒");
        String filePath = "/home/xxx/xxx/" + fileName;
        // 生成一个文件，写入错误数据
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            LogUtils.error("写入文件错误", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                LogUtils.error("关闭错误", e);
            }
        }
        // 上传错误日志文件到文件服务器，我们用的是s3
        // upFileToS3(file, s3Key);
        // 记录上传失败，同时记录错误日志文件地址到数据库，方便用户查看错误信息
        // writeFailToDB(batchNo, s3Key, fileName);
        // 删除文件，防止硬盘爆炸
        // deleteFile(file);
    }
}
