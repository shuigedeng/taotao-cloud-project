package com.taotao.cloud.sys.biz.aop.execl;

import java.util.Date;
import lombok.Data;

@Data
public class ExcelUploadLog {
   private Integer id;
    // 唯一编码
    private String batchNo;
    // 上传到文件服务器的文件key
    private String key;
    // 错误日志文件名
    private String fileName;
    //上传状态
    private Integer status;
    //上传人
    private String createName;
    //上传类型
    private String uploadType;
    //结束时间
    private Date endTime;
    // 开始时间
    private Date startTime;
}
