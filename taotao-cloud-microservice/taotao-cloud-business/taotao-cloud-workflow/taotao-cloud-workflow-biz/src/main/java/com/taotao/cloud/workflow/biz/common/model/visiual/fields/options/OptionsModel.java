package com.taotao.cloud.workflow.biz.common.model.visiual.fields.options;

import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class OptionsModel {
    private Integer id;
    private Integer value;
    private String label;
    private List<OptionsModel> children;
}
