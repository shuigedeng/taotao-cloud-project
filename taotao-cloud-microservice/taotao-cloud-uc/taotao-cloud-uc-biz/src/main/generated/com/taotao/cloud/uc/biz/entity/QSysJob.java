package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysJob is a Querydsl query type for SysJob
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysJob extends EntityPathBase<SysJob> {

    private static final long serialVersionUID = -1813465062L;

    public static final QSysJob sysJob = new QSysJob("sysJob");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final NumberPath<Long> deptId = createNumber("deptId", Long.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final StringPath name = createString("name");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final StringPath tenantId = createString("tenantId");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSysJob(String variable) {
        super(SysJob.class, forVariable(variable));
    }

    public QSysJob(Path<? extends SysJob> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysJob(PathMetadata metadata) {
        super(SysJob.class, metadata);
    }

}

