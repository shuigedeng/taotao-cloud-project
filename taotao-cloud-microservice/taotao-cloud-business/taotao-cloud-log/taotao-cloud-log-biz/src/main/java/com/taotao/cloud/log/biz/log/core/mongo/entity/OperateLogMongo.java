package com.taotao.cloud.log.biz.log.core.mongo.entity;

import com.taotao.cloud.log.biz.log.core.mongo.convert.LogConvert;
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
*
* @author shuigedeng
* @date 2021/12/2
*/
@Data
@Accessors(chain = true)
@Document(collection="starter_audit_operate_log")
public class OperateLogMongo  {

    @Id
    private Long id;

    /** 操作模块 */
    private String title;

    /** 操作人员id */
    private Long operateId;

    /** 操作人员账号 */
    private String username;

    /** 业务类型 */
    private String businessType;

    /** 请求方法 */
    private String method;

    /** 请求方式 */
    private String requestMethod;

    /** 请求url */
    private String operateUrl;

    /** 操作ip */
    private String operateIp;

    /** 操作地点 */
    private String operateLocation;

    /** 请求参数 */
    private String operateParam;

    /** 返回参数 */
    private String operateReturn;

    /** 操作状态（0正常 1异常） */
    private Boolean success;

    /** 错误消息 */
    private String errorMsg;

    /** 操作时间 */
    private LocalDateTime operateTime;

    public OperateLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }
}
