package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysRoleResource is a Querydsl query type for SysRoleResource
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysRoleResource extends EntityPathBase<SysRoleResource> {

    private static final long serialVersionUID = 550325991L;

    public static final QSysRoleResource sysRoleResource = new QSysRoleResource("sysRoleResource");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> resourceId = createNumber("resourceId", Long.class);

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public QSysRoleResource(String variable) {
        super(SysRoleResource.class, forVariable(variable));
    }

    public QSysRoleResource(Path<? extends SysRoleResource> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysRoleResource(PathMetadata metadata) {
        super(SysRoleResource.class, metadata);
    }

}

