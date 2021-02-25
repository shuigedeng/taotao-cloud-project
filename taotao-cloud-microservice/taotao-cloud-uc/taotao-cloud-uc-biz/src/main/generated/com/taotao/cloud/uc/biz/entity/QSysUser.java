package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysUser is a Querydsl query type for SysUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysUser extends EntityPathBase<SysUser> {

    private static final long serialVersionUID = -382510322L;

    public static final QSysUser sysUser = new QSysUser("sysUser");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    public final StringPath avatar = createString("avatar");

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final NumberPath<Long> deptId = createNumber("deptId", Long.class);

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isLock = createBoolean("isLock");

    public final NumberPath<Long> jobId = createNumber("jobId", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> sex = createNumber("sex", Integer.class);

    public final StringPath tenantId = createString("tenantId");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final StringPath username = createString("username");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSysUser(String variable) {
        super(SysUser.class, forVariable(variable));
    }

    public QSysUser(Path<? extends SysUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysUser(PathMetadata metadata) {
        super(SysUser.class, metadata);
    }

}

