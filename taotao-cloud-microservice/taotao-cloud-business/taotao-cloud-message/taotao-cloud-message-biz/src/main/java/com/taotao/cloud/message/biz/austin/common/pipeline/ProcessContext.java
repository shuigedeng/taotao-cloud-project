package com.taotao.cloud.message.biz.austin.common.pipeline;

import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 责任链上下文
 *
 * @author shuigedeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Accessors(chain = true)
public class ProcessContext<T extends ProcessModel> implements Serializable {
    /**
     * 标识责任链的code
     */
    private String code;
    /**
     * 存储责任链上下文数据的模型
     */
    private T processModel;
    /**
     * 责任链中断的标识
     */
    private Boolean needBreak;
    /**
     * 流程处理的结果
     */
    private BasicResultVO response;

}
