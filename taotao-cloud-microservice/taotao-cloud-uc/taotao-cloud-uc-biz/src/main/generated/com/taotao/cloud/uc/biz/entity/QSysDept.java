package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysDept is a Querydsl query type for SysDept
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysDept extends EntityPathBase<SysDept> {

    private static final long serialVersionUID = -383029880L;

    public static final QSysDept sysDept = new QSysDept("sysDept");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final StringPath tenantId = createString("tenantId");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSysDept(String variable) {
        super(SysDept.class, forVariable(variable));
    }

    public QSysDept(Path<? extends SysDept> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysDept(PathMetadata metadata) {
        super(SysDept.class, metadata);
    }

}

