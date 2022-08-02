package com.taotao.cloud.sys.biz.modules.database.service.dtos.compare;

import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class CompareParam {
    @NotNull
    private String baseConnName;
    @NotNull
    private String compareConnName;
    @Valid
    private Namespace baseNamespace;
    @Valid
    private Namespace compareNamespace;

    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase = true;
}
