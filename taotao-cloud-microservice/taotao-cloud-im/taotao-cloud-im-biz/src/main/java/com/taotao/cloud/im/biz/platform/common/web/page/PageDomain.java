package com.taotao.cloud.im.biz.platform.common.web.page;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 分页数据
 */
@Data
@NoArgsConstructor
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 排序列
     */
    private String orderByColumn;
    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    private String isAsc;

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return StrUtil.toUnderlineCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageStart() {
        return (getPageNum() - 1) * getPageSize();
    }

    public Integer getPageEnd() {
        return getPageStart() + getPageSize();
    }

    /**
     * 记录总数
     */
    private Long total;

}
