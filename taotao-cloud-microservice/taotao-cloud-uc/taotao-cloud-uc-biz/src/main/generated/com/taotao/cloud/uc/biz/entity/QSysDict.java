package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysDict is a Querydsl query type for SysDict
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysDict extends EntityPathBase<SysDict> {

    private static final long serialVersionUID = -383026439L;

    public static final QSysDict sysDict = new QSysDict("sysDict");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final StringPath description = createString("description");

    public final StringPath dictCode = createString("dictCode");

    public final StringPath dictName = createString("dictName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSysDict(String variable) {
        super(SysDict.class, forVariable(variable));
    }

    public QSysDict(Path<? extends SysDict> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysDict(PathMetadata metadata) {
        super(SysDict.class, metadata);
    }

}

