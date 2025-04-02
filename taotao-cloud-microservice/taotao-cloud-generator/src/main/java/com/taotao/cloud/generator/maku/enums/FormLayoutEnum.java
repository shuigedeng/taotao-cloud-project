package com.taotao.cloud.generator.maku.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表单布局 枚举
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Getter
@AllArgsConstructor
public enum FormLayoutEnum {
    ONE(1),
    TWO(2);
    
    private final Integer value;
}
