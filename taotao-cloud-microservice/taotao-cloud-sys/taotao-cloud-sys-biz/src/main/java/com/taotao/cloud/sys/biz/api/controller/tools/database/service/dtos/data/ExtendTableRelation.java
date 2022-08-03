package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableRelation;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ExtendTableRelation extends TableRelation {
    private String firstSourcePrimaryKey;
    private String firstTargetPrimaryKey;

    public ExtendTableRelation(TableRelation tableRelation) {
        BeanUtils.copyProperties(tableRelation,this);
    }

    public ExtendTableRelation(TableRelation tableRelation,String firstSourcePrimaryKey,String firstTargetPrimaryKey) {
        BeanUtils.copyProperties(tableRelation,this);
        this.firstSourcePrimaryKey = firstSourcePrimaryKey;
        this.firstTargetPrimaryKey = firstTargetPrimaryKey;
    }

    @Override
    public ExtendTableRelation reverse() {
        return new ExtendTableRelation(super.reverse(),firstTargetPrimaryKey,firstSourcePrimaryKey);
    }
}
