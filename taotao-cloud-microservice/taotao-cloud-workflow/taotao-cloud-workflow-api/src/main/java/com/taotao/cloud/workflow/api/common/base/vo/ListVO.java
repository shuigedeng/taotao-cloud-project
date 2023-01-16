package com.taotao.cloud.workflow.api.common.base.vo;

import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class ListVO<T> {
    private List<T>  list;

}
