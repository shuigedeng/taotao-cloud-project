package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysResource is a Querydsl query type for SysResource
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysResource extends EntityPathBase<SysResource> {

    private static final long serialVersionUID = 378067409L;

    public static final QSysResource sysResource = new QSysResource("sysResource");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    public final BooleanPath alwaysShow = createBoolean("alwaysShow");

    public final StringPath component = createString("component");

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final BooleanPath hidden = createBoolean("hidden");

    public final StringPath icon = createString("icon");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isFrame = createBoolean("isFrame");

    public final BooleanPath keepAlive = createBoolean("keepAlive");

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath path = createString("path");

    public final StringPath perms = createString("perms");

    public final StringPath redirect = createString("redirect");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final StringPath tenantId = createString("tenantId");

    public final NumberPath<Byte> type = createNumber("type", Byte.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSysResource(String variable) {
        super(SysResource.class, forVariable(variable));
    }

    public QSysResource(Path<? extends SysResource> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysResource(PathMetadata metadata) {
        super(SysResource.class, metadata);
    }

}

