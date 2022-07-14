package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.nodejson;

import java.util.Date;
import lombok.Data;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:12
 */
@Data
public class DateProperties {

    /**定时器**/
    private String title;
    private Integer day = 0;
    private Integer hour = 0;
    private Integer minute = 0;
    private Integer second = 0;
    /**判断是否有定时器**/
    private Boolean time = false;
    /**定时器id**/
    private String nodeId;
    /**定时器下一节点**/
    private String nextId;
    /**定时任务结束时间**/
    private Date date;

}
