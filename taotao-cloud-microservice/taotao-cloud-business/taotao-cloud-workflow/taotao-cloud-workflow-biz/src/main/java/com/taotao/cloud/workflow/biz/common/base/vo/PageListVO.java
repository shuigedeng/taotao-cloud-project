package com.taotao.cloud.workflow.biz.common.base.vo;

import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class PageListVO<T> {
    private List<T> list;
    PaginationVO pagination;

}
