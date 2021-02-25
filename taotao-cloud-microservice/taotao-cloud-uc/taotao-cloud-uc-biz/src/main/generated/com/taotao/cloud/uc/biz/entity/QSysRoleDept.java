package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysRoleDept is a Querydsl query type for SysRoleDept
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysRoleDept extends EntityPathBase<SysRoleDept> {

    private static final long serialVersionUID = 452058526L;

    public static final QSysRoleDept sysRoleDept = new QSysRoleDept("sysRoleDept");

    public final NumberPath<Long> deptId = createNumber("deptId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public QSysRoleDept(String variable) {
        super(SysRoleDept.class, forVariable(variable));
    }

    public QSysRoleDept(Path<? extends SysRoleDept> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysRoleDept(PathMetadata metadata) {
        super(SysRoleDept.class, metadata);
    }

}

