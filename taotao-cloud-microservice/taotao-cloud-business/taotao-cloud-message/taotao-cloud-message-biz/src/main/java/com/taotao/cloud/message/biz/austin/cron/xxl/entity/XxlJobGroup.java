package com.taotao.cloud.message.biz.austin.cron.xxl.entity;

import cn.hutool.core.text.StrPool;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;
import lombok.experimental.*;

import java.util.*;

/**
 * 执行器组信息
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class XxlJobGroup {

    private int id;
    private String appname;

    private String title;
    /**
     * 执行器地址类型：0=自动注册、1=手动录入
     */
    private int addressType;

    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String addressList;
    private Date updateTime;

    /**
     * registry list 执行器地址列表(系统注册)
     */
    private List<String> registryList;

    public List<String> getRegistryList() {
        if (Objects.nonNull(addressList) && addressList.trim().length() > 0) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(StrPool.COMMA)));
        }
        return registryList;
    }


}
