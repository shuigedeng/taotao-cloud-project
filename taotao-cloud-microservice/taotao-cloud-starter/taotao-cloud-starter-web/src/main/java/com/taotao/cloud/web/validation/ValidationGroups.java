package com.taotao.cloud.web.validation;

import javax.validation.groups.Default;

/**
 * 校验分组-默认定义
 * <p>校验分组需灵活使用，默认定义只可解决常规场景或作为示例参考</p>
 * <p>使用 {@link javax.validation.GroupSequence} 注解可指定分组校验顺序，同时还拥有短路能力</p>
 * <p>使用 {@link org.hibernate.validator.group.GroupSequenceProvider}
 * 注解可动态指定分组校验顺序，解决多字段组合逻辑校验的痛点问题，但需自行实现
 * {@link org.hibernate.validator.spi.group.DefaultGroupSequenceProvider} 接口</p>
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:19:22
 */
public class ValidationGroups {

    public interface Create extends Default {

    }

    public interface Get extends Default {

    }

    public interface Update extends Default {

    }

    public interface Delete extends Default {

    }

}
