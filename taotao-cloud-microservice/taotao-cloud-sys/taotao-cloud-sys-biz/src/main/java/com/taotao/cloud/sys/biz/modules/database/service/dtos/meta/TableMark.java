package com.taotao.cloud.sys.biz.modules.database.service.dtos.meta;

import com.sanri.tools.modules.core.dtos.DictDto;
import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import lombok.Data;

import javax.validation.Valid;
import java.util.*;

@Data
public class TableMark {
    @Valid
    private ActualTableName actualTableName;
    private Set<String> tags = new HashSet<>();

    public TableMark() {
    }

    public TableMark(ActualTableName actualTableName) {
        this.actualTableName = actualTableName;
    }

    /**
     * 表是否弃用
     * @return
     */
    public boolean isDeprecated(){
        return this.tags != null && tags.contains("deprecated");
    }

    /**
     * 支持的表标签
     */
    public static final String [] supportTags = {"biz","dict","sys","report","biz_config","deprecated","archive"};
//    public static final List<DictDto<String>> supportTags = new ArrayList<>();
//    static {
//        supportTags.add(new DictDto<>("biz","业务表"));
//        supportTags.add(new DictDto<>("dict","字典表"));
//        supportTags.add(new DictDto<>("sys","系统表"));
//        supportTags.add(new DictDto<>("report","数据报表"));
//        supportTags.add(new DictDto<>("biz_config","业务配置表"));
//        supportTags.add(new DictDto<>("deprecated","弃用"));
//        supportTags.add(new DictDto<>("archive","归档表"));
//    }
}
