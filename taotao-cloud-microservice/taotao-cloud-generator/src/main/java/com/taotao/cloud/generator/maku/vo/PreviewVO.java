package com.taotao.cloud.generator.maku.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 预览视图对象
 *
 * @author xiangmeng
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PreviewVO {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件内容
     */
    private String content;


}
